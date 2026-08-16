# KELEM API Contract

**Version:** 1.0 (frozen)  
**Date:** 2026-08-16  
**Owner:** `online-art-gallery-springboot`  
**Consumers:** `vue-oag-frontend` HTTP adapter (live mode)

This document is the source of truth for `/api/v1`. Frontend and backend refactors implement this file. No new path ships without a contract update.

OpenAPI (generated from controllers once they match): `GET /v3/api-docs` · Swagger UI: `/swagger-ui.html`.

**Legend for Status**

- `live` — exists on Spring today at this canonical path (or noted as close)
- `spec` — specified here; backend refactor must implement
- `alias` — legacy path kept until the Vue adapter cutover, then removed

---

## Locked decisions

- Prefix: `/api/v1`. Server port **8088**. No servlet context path.
- Envelope: `{ "status": number, "message": string, "content": T }`. Pageable adds `pageable`.
- Frontend unwraps `content` (and `pageable`). Mock adapter returns unwrapped `content` only.
- Auth: `Authorization: Bearer <jwt>`. Login body: `username`, `password`, `channel`.
- Payments: **Chapa / ETB only**. `POST /checkout` returns `{ checkOutUrl, txRef }`. PayPal is not in this contract.
- Realtime: STOMP/SockJS `/ws/notifications`; user queue `/user/queue/notifications`; topic `/topic/global`.
- Mutations: JSON bodies. Query params for filters and pagination only. Multipart only for artwork (and later profile photo) uploads.
- Public (no JWT): GET catalog, artists, collections, accepted events, public competitions, auction list/detail (not bid), CoA verify, `/upload/**`, `/api/v1/auth/**` except logout, Chapa callback.
- One JSON name per field. Backend names are canonical. Frontend normalizer aliases are listed in [Field aliases](#field-aliases).

---

## A. Envelope, errors, pagination, versioning

### Envelope

```json
{ "status": 200, "message": "Artwork retrieved", "content": {} }
```

Pageable (`GenericResponsePageable`):

```json
{
  "status": 200,
  "message": "Successfully retrieved recent artworks",
  "content": [],
  "pageable": {
    "totalPages": 0,
    "totalElements": 0,
    "numberOfElements": 0,
    "last": true,
    "first": true,
    "empty": true
  }
}
```

HTTP status **must** match `status`. Default paging: `page=0`, `size=20` (also accepted: `pageNumber`, `pageSize` on legacy endpoints).

### Errors

| HTTP | When | `content` |
| --- | --- | --- |
| 400 | Validation (`field: message` joined) | `null` |
| 401 | Missing/invalid/expired JWT; unexpected role on typed login | `null` |
| 403 | Authenticated but missing permission | `null` |
| 404 | Resource not found | `null` |
| 409 | Optimistic lock / bid too low / stock conflict | `null` |
| 413 | Upload over 5 MB | `null` |
| 422 | User not found (email lookup) | `null` |
| 429 | Auth rate limit (30 req/min/IP) | `null` |
| 500 | Unexpected / `GeneralException` | `null` |

### Versioning

- Breaking changes require `/api/v2` or a new frozen minor with a changelog in this file.
- Legacy aliases listed in [H. Legacy aliases](#h-legacy-aliases). Removal target: after Vue live-mode cutover (backend refactor milestone).

### JWT

Access token claims (specified; today only `permissions` is set besides `sub`):

```json
{
  "sub": "customer@gmail.com",
  "role": "CUSTOMER",
  "permissions": ["USER_MODIFY_CART"],
  "iat": 0,
  "exp": 0
}
```

`role` is one of `ADMIN | CUSTOMER | ARTIST | MANAGER | ORGANIZATION` **without** the `ROLE_` prefix. Frontend may still strip `ROLE_` if present. Refresh tokens have `sub` only. Header: `Authorization: Bearer <token>`.

### STOMP

| Item | Value |
| --- | --- |
| Handshake | `GET /ws/notifications` (SockJS). Pass JWT as `Authorization` CONNECT header or `?token=` |
| App prefix | `/app` |
| Broker | `/topic`, `/queue` |
| User queue | `/user/queue/notifications` |
| Broadcast | `/topic/global` |

Payload: `{ "message": string, "type": string, "createdAt": string }`. Used for order/status and (spec) new chat messages.

---

## B. Auth and users

**Decision:** one login for every role. `/api/v1/admin/auth/*` remains an **alias** that still restricts to admin users.

Public signup: `CUSTOMER` or `ARTIST` via `role`. Manager and organization accounts are created by admin register.

### Auth endpoints

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| POST | `/api/v1/auth/login` | public | spec (live exists but `UserType.CUSTOMER` only) | `AuthRequest` | `UserInfo` or empty list + OTP message |
| POST | `/api/v1/auth/login/verify` | public | live | `VerifyOtp` | `UserInfo` |
| POST | `/api/v1/auth/register` | public | spec | `SignupRequest` | empty list (OTP sent) |
| POST | `/api/v1/auth/register/verify` | public | spec | `VerifyOtp` | empty list (activated; no tokens) |
| POST | `/api/v1/auth/password/forgot` | public | live | `{ "email", "resendOtp"? }` | empty list |
| POST | `/api/v1/auth/password/reset` | public | live | `{ "otp", "email", "password", "confirmPassword" }` | empty list |
| POST | `/api/v1/auth/token/refresh` | public | live | `{ "refreshToken" }` | `UserInfo` |
| POST | `/api/v1/auth/logout` | JWT | spec | empty | empty list |
| POST | `/api/v1/auth/password/change` | JWT + `USER_MODIFY_PROFILE` | spec | `{ "currentPassword", "password", "confirmPassword" }` | empty list |

If 2FA is enabled, login returns HTTP 200, `message` containing `Otp`, and `content: []`. Client then calls `/login/verify`.

### Auth DTOs

**AuthRequest**

```json
{ "username": "user@email", "password": "********", "channel": "EMAIL" }
```

`channel`: `EMAIL | SMS`.

**SignupRequest**

```json
{
  "firstName": "Ada",
  "lastName": "Kebede",
  "email": "ada@email",
  "password": "********",
  "confirmPassword": "********",
  "phone": "0911223344",
  "channel": "EMAIL",
  "role": "CUSTOMER",
  "sex": "female",
  "age": 30,
  "photo": null
}
```

`role` optional, default `CUSTOMER`. Allowed on public signup: `CUSTOMER | ARTIST`.

**VerifyOtp**

```json
{
  "otp": "123456",
  "username": "ada@email",
  "phone": null,
  "rememberMe": false,
  "resendOtp": false,
  "medium": "EMAIL"
}
```

**UserInfo** (`content` on login/refresh)

```json
{
  "uuid": "…",
  "token": "<jwt>",
  "refreshToken": "<jwt>",
  "username": "ada@email",
  "permissions": ["USER_MODIFY_CART"],
  "fullName": "Ada Kebede",
  "avatarUrl": "/upload/images/…",
  "role": "CUSTOMER"
}
```

`role` on `UserInfo` is **specified** (not in today's DTO). JWT `role` claim is the fallback.

### Profile and admin users

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/users/me` | JWT + `USER_MODIFY_PROFILE` | spec | — | `UserProfile` |
| PATCH | `/api/v1/users/me` | JWT + `USER_MODIFY_PROFILE` | spec | `UserProfileUpdate` | `UserProfile` |
| POST | `/api/v1/users/me/photo` | JWT + `USER_MODIFY_PROFILE` | spec | multipart `file` | `UserProfile` |
| GET | `/api/v1/users/me/notifications` | JWT | spec | — | `Notification[]` |
| PATCH | `/api/v1/users/me/notifications/{id}/read` | JWT | spec | — | `Notification` |
| POST | `/api/v1/admin/register` | JWT `ADMIN` | live | `RegisterUserRequest` (`roleId` required) | empty list |
| POST | `/api/v1/admin/register/verify` | JWT `ADMIN` | live | `VerifyOtp` | empty list |
| GET | `/api/v1/admin/users/all` | JWT `ADMIN` | live | query filters + paging | pageable `User[]` |
| GET | `/api/v1/admin/users/role` | JWT `ADMIN` | live | `uuid` used as role name + paging | pageable `User[]` |
| DELETE | `/api/v1/users/{id}` | `ADMIN_MODIFY_USER` | live (must return envelope, not raw string) | — | `null` |
| GET | `/api/v1/admin/dashboard/kpis` | `ADMIN_VIEW_DASHBOARD` | live | — | `AdminDashboardKpis` |
| GET | `/api/v1/admin/permissions` | `ADMIN_MANAGE_PERMISSIONS` | live | — | permission list |
| PUT | `/api/v1/admin/permissions/roles/{roleId}` | `ADMIN_MANAGE_PERMISSIONS` | live | `number[]` permission ids | assigned list |

**UserProfile**

```json
{
  "id": 1,
  "uuid": "…",
  "username": "ada@email",
  "email": "ada@email",
  "firstName": "Ada",
  "lastName": "Kebede",
  "role": "CUSTOMER",
  "phone": "0911223344",
  "address": null,
  "avatarUrl": "/upload/images/…",
  "bio": null,
  "verifiedArtist": false,
  "slug": "ada-kebede"
}
```

**AdminDashboardKpis:** `totalUsers`, `totalOrders`, `pendingArtworks`, `pendingPayouts`, `totalRevenue`.

**RegisterUserRequest:** `firstName`, `lastName`, `email`, `password`, `confirmPassword`, `phone`, `roleId`, `channel`.

### Roles and permissions

Roles (JWT / `UserInfo.role`): `ADMIN`, `CUSTOMER`, `ARTIST`, `MANAGER`, `ORGANIZATION`.  
Stored as `ROLE_ADMIN`, `ROLE_CUSTOMER`, `ROLE_ARTIST`, `ROLE_MANAGER` (`spec`), `ROLE_ORGANIZATION` (`spec`).

| Role | Permissions |
| --- | --- |
| CUSTOMER | `USER_MODIFY_CART`, `USER_ADD_WISHLIST`, `USER_FETCH_WISHLIST`, `USER_DELETE_WISHLIST`, `USER_ADD_ORDER`, `USER_VIEW_ORDERS`, `USER_MODIFY_PROFILE`, `CUSTOMER_BROWSE_ARTWORK`, `USER_ADD_COMPETITOR`, `USER_MODIFY_COMPETITOR`, `USER_RATE_ARTWORK`, `USER_FOLLOW_ARTIST`, `USER_MAKE_OFFER`, `USER_PLACE_BID`, `USER_MESSAGE` (`spec`) |
| ARTIST | customer set plus `ARTIST_SUBMIT_ARTWORK`, `ARTIST_BROWSE_ARTWORK`, `ARTIST_VIEW_OWN_ARTWORK`, `ARTIST_REQUEST_PAYOUT`, `ARTIST_ACCEPT_OFFER` (`spec`) |
| MANAGER | `ADMIN_FETCH_ARTWORK`, `ADMIN_MODIFY_ARTWORK`, `ADMIN_FETCH_COMPETITION`, `ADMIN_ADD_COMPETITION`, `ADMIN_MODIFY_COMPETITION`, `ADMIN_DELETE_COMPETITION`, `ADMIN_FETCH_COMPETITOR`, `ADMIN_ADD_STANDARD`, `ADMIN_MODIFY_STANDARD`, `ADMIN_FETCH_EVENT`, `ADMIN_MODIFY_EVENT` |
| ORGANIZATION | `ORG_ADD_EVENT` (`spec`), `ORG_MODIFY_OWN_EVENT` (`spec`), `ADMIN_FETCH_EVENT` (own), `USER_MODIFY_PROFILE` |
| ADMIN | all `ADMIN_*` including `ADMIN_MODIFY_USER`, `ADMIN_FETCH_ARTWORK`, `ADMIN_MODIFY_ARTWORK`, `ADMIN_DELETE_ARTWORK`, `ADMIN_FETCH_ORDERS`, `ADMIN_MODIFY_ORDER`, `ADMIN_DELETE_ORDER`, `ADMIN_FETCH_REPORT`, `ADMIN_DELETE_REPORT`, `ADMIN_FETCH_COMPETITION`, `ADMIN_ADD_COMPETITION`, `ADMIN_MODIFY_COMPETITION`, `ADMIN_DELETE_COMPETITION`, `ADMIN_FETCH_COMPETITOR`, `ADMIN_DELETE_COMPETITOR`, `ADMIN_ADD_EVENT`, `ADMIN_FETCH_EVENT`, `ADMIN_MODIFY_EVENT`, `ADMIN_DELETE_EVENT`, `ADMIN_ADD_STANDARD`, `ADMIN_MODIFY_STANDARD`, `ADMIN_MANAGE_PERMISSIONS`, `ADMIN_MANAGE_PAYOUTS`, `ADMIN_VIEW_DASHBOARD` |

---

## C. Catalog (public GET)

Image URLs are relative, served publicly: `{origin}/upload/images/{uuid}_{filename}`. Max 5 MB; types `image/jpeg`, `image/png`, `image/gif`.

### Artworks

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/artworks` | public | spec | `page`, `size`, `artworkCategory`, `minPrice`, `maxPrice`, `sortBy` | pageable `Artwork[]` (status `ACCEPTED` only) |
| GET | `/api/v1/artworks/{id}` | public | spec | — | `Artwork` (records a view if JWT present) |
| GET | `/api/v1/artworks/search` | public | spec | `artworkCategory`, `artworkName`, `minPrice`, `maxPrice`, `sortBy`, `fromDate`, `toDate`, `page`, `size` | pageable `Artwork[]` |
| GET | `/api/v1/artworks/recent` | public | spec | paging | pageable `Artwork[]` |
| GET | `/api/v1/artworks/trending` | public | spec | `limit` default 10 | `Artwork[]` |
| GET | `/api/v1/artworks/category/count` | public | spec | — | `{ [category]: number }` |
| POST | `/api/v1/artworks` | `ARTIST_SUBMIT_ARTWORK` | spec | multipart `ArtworkRequest` | `Artwork` 201, status `PENDING` |
| GET | `/api/v1/artworks/mine` | `ARTIST_VIEW_OWN_ARTWORK` | spec | — | `Artwork[]` (all statuses) |
| PATCH | `/api/v1/artworks/{id}` | owner artist or `ADMIN_MODIFY_ARTWORK` | spec | JSON subset of artwork fields | `Artwork` |
| PATCH | `/api/v1/admin/artwork/change/status/{id}` | `ADMIN_MODIFY_ARTWORK` (MANAGER included) | live | query `status` | `Artwork` |
| GET | `/api/v1/admin/artwork` | `ADMIN_FETCH_ARTWORK` | live | — | `Artwork[]` |
| GET | `/api/v1/admin/artwork/status` | `ADMIN_FETCH_ARTWORK` | live | `status` | `Artwork[]` |
| DELETE | `/api/v1/admin/artwork/{id}` | `ADMIN_DELETE_ARTWORK` | live | — | `null` |

**Artwork** (canonical `content` item — `id` is required; today's DTO omits it)

```json
{
  "id": 1,
  "artworkName": "Blue Nile",
  "artworkDescription": "…",
  "artworkCategory": "Painting",
  "price": 15000.00,
  "size": "80x120 cm",
  "imageUrls": ["/upload/images/uuid_file.jpg"],
  "artistId": 9,
  "artistName": "Ada Kebede",
  "artistSlug": "ada-kebede",
  "status": "ACCEPTED",
  "quantity": 1,
  "medium": "Oil on canvas",
  "yearCreated": 2024,
  "dimensions": "80x120 cm",
  "framing": "Unframed",
  "editionNumber": null,
  "editionSize": null,
  "rejectionReason": null,
  "averageRating": 4.5,
  "allowOffers": true
}
```

`price` is a JSON number (`BigDecimal`), not `int`. `status`: `ACCEPTED | REJECTED | PENDING | SOLD`.

**ArtworkRequest** (multipart submit): `imageFiles`, `artworkName`, `artworkDescription`, `artworkCategory`, `price`, `size`, `quantity`, plus V6 fields `medium`, `yearCreated`, `dimensions`, `framing`, `editionNumber`, `editionSize`.

### Artists

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/artists` | public | spec | paging | pageable `ArtistProfile[]` |
| GET | `/api/v1/artists/{slug}` | public | spec | slug or numeric `id` if all digits | `ArtistProfile` |
| GET | `/api/v1/artists/uuid/{uuid}` | public | live (auth today) | — | `ArtistProfile` |
| GET | `/api/v1/artists/{id}/portfolio` | public | spec | `page`, `size` | pageable artwork summaries |
| POST | `/api/v1/artists/{id}/follow` | `USER_FOLLOW_ARTIST` | live | — | `null` 201 |
| DELETE | `/api/v1/artists/{id}/follow` | `USER_FOLLOW_ARTIST` | live | — | `null` |
| GET | `/api/v1/artists/followed` | JWT | spec | — | `ArtistProfile[]` |

**ArtistProfile:** `id`, `uuid`, `slug`, `firstName`, `lastName`, `bio`, `profilePictureUrl`, `verifiedArtist`, `artworks[]` (`id`, `title`, `imageUrl`, `category`, `price`, `available`).

### Collections

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/collections` | public | spec | `featuredOnly` default false | `Collection[]` |
| GET | `/api/v1/collections/{slug}` | public | spec | — | `Collection` with nested `artworks` |

**Collection:** `id`, `slug`, `title`, `description`, `featured`, `creationDate`, `artworks` (`Artwork[]` on detail).

---

## D. Commerce

Currency for money in this contract is **ETB**. Frontend mock may still display a display currency; live checkout is Chapa ETB.

### Concurrency

- `Artwork`, `Auction`, and `ArtistWallet` use optimistic locking (`version`). Stale updates return **409**.
- Place-bid must exceed `currentBid` (or `reservePrice` if no bids) inside a transaction.
- Checkout fulfill is **idempotent** on `tx_ref` (no double stock decrement, no double wallet credit).
- Anti-snipe: if a bid arrives in the last **5 minutes**, `endTime` extends by 5 minutes.

### Cart

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/cart` | `USER_MODIFY_CART` | live | — | `CartItem[]` |
| POST | `/api/v1/cart` | `USER_MODIFY_CART` | spec | `{ "artworkId": 1, "quantity": 1 }` | `CartItem` 201 (merge on user+artwork) |
| PATCH | `/api/v1/cart/{id}` | `USER_MODIFY_CART` | spec | `{ "quantity": 2 }` | `CartItem` |
| DELETE | `/api/v1/cart/{id}` | `USER_MODIFY_CART` | live | — | `null` |
| DELETE | `/api/v1/cart` | `USER_MODIFY_CART` | spec (live is `DELETE /cart/clear`) | — | `null` |
| GET | `/api/v1/cart/total` | `USER_MODIFY_CART` | live | — | number |

**CartItem:** `id`, `quantity`, `artwork` (`Artwork`). Unique `(userId, artworkId)`. Insufficient stock → 400.

### Wishlist

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/wishlist` | `USER_FETCH_WISHLIST` | live | — | `WishlistItem[]` (many rows per user) |
| POST | `/api/v1/wishlist/{artworkId}` | `USER_ADD_WISHLIST` | spec | — | `WishlistItem` |
| DELETE | `/api/v1/wishlist/{id}` | `USER_DELETE_WISHLIST` | live | wishlist row id | `null` |
| GET | `/api/v1/wishlist/check/{artworkId}` | `USER_FETCH_WISHLIST` | spec | — | `{ "inWishlist": true, "id": 1 }` |

Unique `(userId, artworkId)`. Duplicate POST is idempotent (return existing).

### Checkout, orders, Chapa

Buyer path is **`POST /checkout` only**. Do not use `POST /orders` for paid checkout.

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| POST | `/api/v1/checkout` | `USER_ADD_ORDER` | live | `OrderRequest` | `PaymentInit` |
| GET | `/api/v1/chapa/callback` | public | live | query `tx_ref` | `PaymentStatus` |
| GET | `/api/v1/orders` | `USER_VIEW_ORDERS` | spec (live: `/orders/my`) | — | `Order[]` |
| GET | `/api/v1/orders/{id}` | `USER_VIEW_ORDERS` (owner) or `ADMIN_FETCH_ORDERS` | spec | — | `Order` |
| GET | `/api/v1/orders/admin` | `ADMIN_FETCH_ORDERS` | live | — | `Order[]` |
| PATCH | `/api/v1/orders/admin/{id}/status` | `ADMIN_MODIFY_ORDER` | live | query `status` or body `{ "status" }` | `Order` |
| DELETE | `/api/v1/orders/admin/{id}` | `ADMIN_DELETE_ORDER` | live | — | `null` |

**OrderRequest**

```json
{
  "firstname": "Ada",
  "lastname": "Kebede",
  "email": "ada@email",
  "phone": "0911223344",
  "address": {
    "street": "Bole Rd",
    "city": "Addis Ababa",
    "state": "AA",
    "country": "ET",
    "postalCode": "1000"
  }
}
```

**PaymentInit:** `{ "checkOutUrl": "https://…", "txRef": "…" }` (do not expose raw `paymentLog` to the client).

**PaymentStatus:** `VERIFIED | INITIALIZED | FAILED`. (`INTIALIZED` is legacy DB only.)

Callback: if verified → order `CONFIRMED`, decrement stock, clear **that order's** cart lines, issue CoA, credit wallets. Else `CANCELLED`. Abandoned `PENDING` orders are swept after a documented TTL (spec: 60 minutes).

**Order**

```json
{
  "id": 10,
  "firstname": "Ada",
  "lastname": "Kebede",
  "email": "ada@email",
  "phone": "0911223344",
  "status": "PENDING",
  "totalAmount": 15000.00,
  "orderDate": "2026-08-16T08:00:00.000+00:00",
  "secretCode": "AB12CD",
  "items": [
    {
      "id": 1,
      "artworkId": 1,
      "artistId": 9,
      "quantity": 1,
      "unitPrice": 15000.00,
      "lineTotal": 15000.00,
      "artworkName": "Blue Nile",
      "imageUrl": "/upload/images/…"
    }
  ],
  "address": { "street": "Bole Rd", "city": "Addis Ababa", "state": "AA", "country": "ET", "postalCode": "1000" },
  "shipment": null,
  "certificates": []
}
```

`status`: `PENDING | CONFIRMED | PROCESSING | SHIPPED | DELIVERED | CANCELLED`.

### Offers

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| POST | `/api/v1/offers` | `USER_MAKE_OFFER` | spec (live uses query params) | `{ "artworkId": 1, "amount": 12000 }` | `Offer` 201 |
| GET | `/api/v1/offers/artwork/{artworkId}` | owner `ARTIST_VIEW_OWN_ARTWORK` or admin | live | — | `Offer[]` |
| GET | `/api/v1/offers/my` | `USER_MAKE_OFFER` | live | — | `Offer[]` |
| GET | `/api/v1/offers/pending` | `ARTIST_ACCEPT_OFFER` | spec | — | pending offers on the artist's works |
| PATCH | `/api/v1/offers/{id}/status` | owner `ARTIST_ACCEPT_OFFER` or `ADMIN_MODIFY_ARTWORK` | spec | `{ "status": "ACCEPTED" }` | `Offer` |

`Offer`: `id`, `artworkId`, `buyerId`, `amount`, `status` (`PENDING | ACCEPTED | REJECTED | WITHDRAWN`), `createdAt`.  
Accepting an offer creates a `PENDING` order at `amount` and returns it (or a checkout `PaymentInit` if the client should pay immediately). Spec: return `{ "offer", "orderId", "payment" }` where `payment` may be null until the buyer calls checkout for that order.

### Auctions

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/auctions` | public | spec (live requires JWT) | — | `Auction[]` (active) |
| GET | `/api/v1/auctions/{id}` | public | spec | — | `Auction` |
| GET | `/api/v1/auctions/{id}/bids` | public | spec | — | `Bid[]` |
| POST | `/api/v1/auctions` | `ADMIN_MODIFY_ARTWORK` or owning artist | spec (live query params + admin only) | `{ "artworkId", "startTime", "endTime", "reservePrice"? }` | `Auction` 201 |
| POST | `/api/v1/auctions/{id}/bid` | `USER_PLACE_BID` | spec (live query `amount`) | `{ "amount": 16000 }` | `Bid` 201 |
| POST | `/api/v1/auctions/{id}/watch` | JWT | spec | — | `null` |
| DELETE | `/api/v1/auctions/{id}/watch` | JWT | spec | — | `null` |

**Auction:** `id`, `artworkId`, `artwork`, `startTime`, `endTime`, `reservePrice`, `currentBid`, `status` (`ACTIVE | ENDED | CANCELLED`), `winnerId`, `version`.  
**Bid:** `id`, `auctionId`, `bidderId`, `amount`, `bidTime`.

When an auction ends with `currentBid >= reservePrice` (or no reserve), the winner gets a payable order. Client uses `POST /checkout` with that pending order context **or** the close job returns a `PaymentInit` (backend refactor picks one; default: create `PENDING` order, notify winner via STOMP, winner checks out).

### Payouts

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/payouts/wallet` | `ARTIST_REQUEST_PAYOUT` | live | — | `Wallet` |
| POST | `/api/v1/payouts/request` | `ARTIST_REQUEST_PAYOUT` | spec (live query `amount`) | `{ "amount": 5000 }` | `PayoutRequest` 201 |
| GET | `/api/v1/payouts/my` | `ARTIST_REQUEST_PAYOUT` | live | — | `PayoutRequest[]` |
| GET | `/api/v1/payouts/admin/pending` | `ADMIN_MANAGE_PAYOUTS` | live | — | `PayoutRequest[]` |
| PATCH | `/api/v1/payouts/admin/{id}/approve` | `ADMIN_MANAGE_PAYOUTS` | live | — | `PayoutRequest` |

**Wallet:** `id`, `artistId`, `balance`, `pendingBalance`, `version`.  
**PayoutRequest:** `id`, `artistId`, `amount`, `status` (`PENDING | APPROVED | REJECTED | PAID`), `requestedAt`, `processedAt`, `externalRef`.  
Approve must not mark `PAID` without an external transfer id (Chapa/bank). Until wired, status stays `APPROVED`.

### Shipments

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| PATCH | `/api/v1/shipments/{orderId}/ship` | `ADMIN_MODIFY_ORDER` | spec (live query params) | `{ "carrier", "trackingNumber" }` | `Shipment` |
| GET | `/api/v1/shipments/{orderId}/tracking` | `USER_VIEW_ORDERS` or `ADMIN_FETCH_ORDERS` | live | — | `Shipment` |

**Shipment:** `id`, `orderId`, `carrier`, `trackingNumber`, `status` (`PENDING | SHIPPED | IN_TRANSIT | DELIVERED`), `shippedAt`, `deliveredAt`.

### Certificate of authenticity

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/certificates/{code}` | public | spec | verification code | `Certificate` |
| GET | `/api/v1/orders/{id}/certificates` | owner or admin | spec | — | `Certificate[]` |

**Certificate:** `id`, `orderItemId`, `artworkId`, `verificationCode`, `issuedAt`. Issued only after payment verify.

---

## E. Community and ops

### Messages (`spec` — no controller today)

| Method | Path | Auth | Request | `content` |
| --- | --- | --- | --- | --- |
| GET | `/api/v1/messages/threads` | JWT + `USER_MESSAGE` | — | `Thread[]` |
| GET | `/api/v1/messages/threads/{threadId}` | JWT (participant) | — | `Thread` with `messages[]` |
| POST | `/api/v1/messages/threads` | JWT + `USER_MESSAGE` | `{ "artistId", "artworkId"?, "body" }` | `Thread` 201 |
| POST | `/api/v1/messages/threads/{threadId}` | JWT (participant) | `{ "body" }` | `Message` 201 |

**Thread:** `threadId`, `artworkId`, `artistId`, `buyerId`, `unread`, `lastMessage`.  
**Message:** `id`, `threadId`, `senderId`, `senderName`, `body`, `createdAt`, `read`.  
New messages also push on `/user/queue/notifications`.

### Reviews and ratings

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/artworks/{id}/reviews` | public | spec | — | `Review[]` |
| POST | `/api/v1/artworks/{id}/reviews` | `USER_RATE_ARTWORK` | spec | `{ "rating": 5, "comment": "…" }` | `Review` 201 |
| POST | `/api/v1/ratings` | `USER_RATE_ARTWORK` | spec (live query params) | `{ "artworkId", "ratingValue" }` | `Rating` |
| GET | `/api/v1/ratings/artwork/{artworkId}` | public | spec | — | `Rating[]` |
| GET | `/api/v1/ratings/artwork/{artworkId}/average` | public | spec | — | number |

Purchased-only for `POST` reviews (`USER_RATE_ARTWORK`). **Review:** `id`, `artworkId`, `userId`, `userName`, `rating`, `comment`, `createdAt`.

### Events and tickets

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/events` | public | spec (live admin-only) | optional `status` | `Event[]` (`ACCEPTED` if public) |
| GET | `/api/v1/events/{id}` | public for ACCEPTED | spec | — | `Event` |
| POST | `/api/v1/events` | `ORG_ADD_EVENT` or `ADMIN_ADD_EVENT` | spec | `EventRequest` | `Event` 201 `PENDING` |
| PATCH | `/api/v1/events/{id}` | owner org or `ADMIN_MODIFY_EVENT` | live | `EventRequest` | `Event` |
| PATCH | `/api/v1/events/change/status/{id}` | `ADMIN_MODIFY_EVENT` / MANAGER | live | query `status` | `Event` |
| DELETE | `/api/v1/events/{id}` | `ADMIN_DELETE_EVENT` | live | — | `null` |
| POST | `/api/v1/event-tickets/{eventId}` | `USER_ADD_ORDER` | live | — | `EventTicket` |
| GET | `/api/v1/event-tickets/my` | `USER_ADD_ORDER` | live | — | `EventTicket[]` |

**Event:** `id`, `eventName`, `eventDescription`, `location`, `ticketPrice`, `capacity`, `status` (`ACCEPTED | PENDING | REJECTED`), `imageUrl` (filesystem `/upload/…`, not Postgres OID), `eventDate`.

### Competitions

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/competitions` | public | spec | — | `Competition[]` |
| GET | `/api/v1/competitions/{id}` | public | spec | — | `Competition` |
| POST | `/api/v1/competitions` | `ADMIN_ADD_COMPETITION` (MANAGER) | spec | `CompetitionDto` | `Competition` |
| PUT | `/api/v1/competitions/{id}` | `ADMIN_MODIFY_COMPETITION` | spec | `CompetitionDto` | `Competition` |
| DELETE | `/api/v1/competitions/{id}` | `ADMIN_DELETE_COMPETITION` | spec | — | `null` |
| POST | `/api/v1/competitors/register` | `USER_ADD_COMPETITOR` | live | `CompetitorRequest` | `Competitor` |
| POST | `/api/v1/competitors/vote` | JWT (not `USER_MODIFY_COMPETITOR` only) | spec | `{ "competitionId", "competitorId" }` | `null` |
| GET | `/api/v1/competitors` | `ADMIN_FETCH_COMPETITOR` | live | — | `Competitor[]` |
| GET | `/api/v1/competitors/winner/{competitionId}` | public or JWT | spec | — | winners |

One vote per `(userId, competitionId)`. **CompetitionDto** fields: `competitionTitle`, `competitionDescription`, `numberOfCompetitor`, `expiryDate`, `endTime`. **CompetitorRequest:** `artistId`, `competitionId`, `imageUrl`, `artDescription`, `category`.

### Standards, reports, contact, CMS

| Method | Path | Auth | Status | Request | `content` |
| --- | --- | --- | --- | --- | --- |
| GET | `/api/v1/standards` | public | spec (live JWT) | — | `Standard[]` |
| GET | `/api/v1/standards/type` | public | spec | `standardType` | `Standard[]` |
| POST | `/api/v1/standards` | `ADMIN_ADD_STANDARD` | spec (live `/standards/add`) | `{ "standardDescription", "standardType" }` | `Standard` |
| PUT | `/api/v1/standards/{id}` | `ADMIN_MODIFY_STANDARD` | live | same | `Standard` |
| DELETE | `/api/v1/standards/{id}` | `ADMIN_MODIFY_STANDARD` | live | — | `null` |
| POST | `/api/v1/report` | JWT | spec (live `/report/create`) | `Report` | `Report` |
| GET | `/api/v1/report/all` | `ADMIN_FETCH_REPORT` | live | — | `Report[]` |
| GET | `/api/v1/report/{id}` | `ADMIN_FETCH_REPORT` | live | — | `Report` |
| DELETE | `/api/v1/report/{id}` | `ADMIN_DELETE_REPORT` | live | — | `null` |
| POST | `/api/v1/contact` | public | spec | `{ "name", "email", "message" }` | `null` |
| GET | `/api/v1/cms/config` | public GET; PUT admin | spec | — | CMS key/value object |
| PUT | `/api/v1/cms/config` | `ADMIN` | spec | object | object |

`standardType`: `CUSTOMER | ARTIST | ARTWORK | ORGANIZATION`.  
**Report:** `reportDetail`, `reportTitle`, `reportName` (reporter name), `reporterEmail`.

### Moderation (Vue `/moderation/*`)

Canonical admin artwork status endpoints **are** moderation. Aliases for the Vue adapter:

| Method | Path | Auth | Status | Maps to |
| --- | --- | --- | --- | --- |
| GET | `/api/v1/moderation/queue` | `ADMIN_FETCH_ARTWORK` | spec | `GET /admin/artwork/status?status=PENDING` |
| PUT | `/api/v1/moderation/{id}/approve` | `ADMIN_MODIFY_ARTWORK` | spec | status `ACCEPTED` |
| PUT | `/api/v1/moderation/{id}/reject` | `ADMIN_MODIFY_ARTWORK` | spec | body `{ "rejectionReason" }` → status `REJECTED` |

---

## Field aliases

Canonical JSON (left) is what the API returns. Vue `normalizers.js` may map to the right-hand name for UI/mock compatibility.

| Canonical | Frontend alias |
| --- | --- |
| `artworkName` | `title` |
| `artworkDescription` | `description` |
| `artworkCategory` | `category` |
| `imageUrls[0]` | `imageUrl` |
| `averageRating` | `rating` |
| `firstname` (orders) | `firstName` |
| `lastname` (orders) | `lastName` |
| `totalAmount` | `total` |
| `orderDate` | `createdAt` |
| `startTime` / `endTime` (auctions) | `startsAt` / `endsAt` |
| `profilePictureUrl` | `avatar` |
| `verifiedArtist` | `verified` |
| `token` | `accessToken` |
| `username` (UserInfo) | `email` / `user.username` |
| `fullName` | split to `firstName` + `lastName` if needed |

Do **not** dual-write both names in JSON unless a DTO adds `@JsonAlias` (not required for 1.0).

---

## H. Legacy aliases

Keep until Vue `httpAdapter` speaks only canonical paths. Then delete.

| Legacy | Canonical |
| --- | --- |
| `POST /api/v1/auth/customer/signup` | `POST /api/v1/auth/register` |
| `POST /api/v1/auth/customer/signup/verify` | `POST /api/v1/auth/register/verify` |
| `POST /api/v1/admin/auth/login` | `POST /api/v1/auth/login` (any role) |
| `POST /api/v1/admin/auth/login/verify` | `POST /api/v1/auth/login/verify` |
| `POST /api/v1/admin/auth/password/forgot` | `POST /api/v1/auth/password/forgot` |
| `POST /api/v1/admin/auth/password/reset` | `POST /api/v1/auth/password/reset` |
| `POST /api/v1/logout` | `POST /api/v1/auth/logout` |
| `GET/POST /api/v1/user/artwork/**` | `/api/v1/artworks/**` |
| `POST /api/v1/user/artwork/submit` | `POST /api/v1/artworks` |
| `GET /api/v1/user/artwork/myArtwork` | `GET /api/v1/artworks/mine` |
| `GET /api/v1/user/artwork/{id}` | `GET /api/v1/artworks/{id}` |
| `GET /api/v1/user/artwork/recent` | `GET /api/v1/artworks/recent` |
| `GET /api/v1/user/artwork/search` | `GET /api/v1/artworks/search` |
| `GET /api/v1/discovery/collections` | `GET /api/v1/collections` |
| `GET /api/v1/discovery/trending` | `GET /api/v1/artworks/trending` |
| `POST /api/v1/cart/add` | `POST /api/v1/cart` |
| `DELETE /api/v1/cart/clear` | `DELETE /api/v1/cart` |
| `POST /api/v1/wishlist/save/{artworkId}` | `POST /api/v1/wishlist/{artworkId}` |
| `GET /api/v1/orders/my` | `GET /api/v1/orders` |
| `POST /api/v1/orders` (unpaid, no Chapa) | do not use for checkout; `POST /checkout` |
| `GET /api/v1/competition/all` | `GET /api/v1/competitions` |
| `POST /api/v1/competition/add` | `POST /api/v1/competitions` |
| `POST /api/v1/standards/add` | `POST /api/v1/standards` |
| `POST /api/v1/report/create` | `POST /api/v1/report` |
| `POST /api/v1/events/create` | `POST /api/v1/events` |
| `GET /api/v1/artists/{id}` (numeric) | still valid; `{slug}` also accepted |

**Out of contract (removed, never implement):** `POST /paypal/pay`, `POST /paypal/capture`, `POST /bid/saveBidArt`, Socket.IO.

---

## F. Mapping appendix

### Frontend `httpAdapter` today → canonical

Paths below are relative to Axios `baseURL`, which **must** be `http://localhost:8088/api/v1` (prod: `{PUBLIC_API_URL}/api/v1`).

**auth**

- `POST /auth/login` → `POST /auth/login` (send `channel`; handle OTP)
- `POST /auth/register` → `POST /auth/register` (today backend is `/auth/customer/signup`)
- `POST /auth/logout` → `POST /auth/logout` (today backend is `/api/v1/logout`)
- `POST /auth/forgot-password` → `POST /auth/password/forgot`
- `GET /auth/confirm/{token}` → **removed**; use `POST /auth/register/verify`
- `POST /auth/change-password` → `POST /auth/password/change`
- `POST /auth/confirm-registration` → `POST /auth/register/verify`
- *(add)* `POST /auth/login/verify`, `POST /auth/token/refresh`

**artwork**

- `GET /artworks` → `GET /artworks`
- `GET /artworks/{id}` → `GET /artworks/{id}`
- `POST /artworks` → `POST /artworks` (multipart, not JSON)
- `PUT /artworks/{id}` → `PATCH /artworks/{id}`
- `DELETE /artworks/{id}` → `DELETE /admin/artwork/{id}` (admin)
- `GET /artworks/search?q=` → `GET /artworks/search?artworkName=`
- `GET /artworks/recent?limit=` → `GET /artworks/recent` (paging)
- `GET /artworks/category/{category}` → `GET /artworks?artworkCategory=`
- `POST /artworks/{id}/rate` → `POST /ratings` body `{ artworkId, ratingValue }`
- `GET /artworks/pending` → `GET /moderation/queue` or `GET /admin/artwork/status?status=PENDING`
- `PUT /artworks/{id}/accept` → `PUT /moderation/{id}/approve`
- `PUT /artworks/{id}/reject` → `PUT /moderation/{id}/reject`
- `GET /artworks/priceRange` → `GET /artworks?minPrice&maxPrice`
- `GET /artworks/sort` → `GET /artworks?sortBy=`
- `GET /artworks/{id}/image` → **removed**; use `imageUrls[0]` on `{origin}`

**cart**

- `GET /cart` → `GET /cart`
- `POST /cart` `{ artworkId, quantity }` → `POST /cart` (same; backend today is `/cart/add` query)
- `PUT /cart/{itemId}` → `PATCH /cart/{id}`
- `DELETE /cart/{itemId}` → `DELETE /cart/{id}`
- `DELETE /cart` → `DELETE /cart` (today `/cart/clear`)

**wishlist**

- `GET /wishlist` → `GET /wishlist`
- `POST /wishlist/save` (artwork body) → `POST /wishlist/{artworkId}`
- `DELETE /wishlist/{id}` → `DELETE /wishlist/{id}`
- `GET /wishlist/check/{artworkId}` → `GET /wishlist/check/{artworkId}`

**order / payment**

- `GET /orders` → `GET /orders`
- `GET /orders/{id}` → `GET /orders/{id}`
- `POST /orders` → **`POST /checkout`** for paid flow
- `PUT /orders/{id}/status` → `PATCH /orders/admin/{id}/status` (admin)
- `POST /paypal/pay` → **removed**; `POST /checkout`
- `POST /paypal/capture` → **removed**; Chapa callback

**artist / collection / auction / offer**

- `GET /artists` → `GET /artists`
- `GET /artists/{slug}` → `GET /artists/{slug}`
- `POST /artists/{id}/follow` → same
- `DELETE /artists/{id}/follow` → same
- `GET /artists/followed` → `GET /artists/followed`
- `GET /collections` → `GET /collections`
- `GET /collections/{slug}` → `GET /collections/{slug}`
- `GET /auctions` → `GET /auctions`
- `GET /auctions/{id}` → `GET /auctions/{id}`
- `POST /auctions` JSON → `POST /auctions`
- `POST /auctions/{id}/bid` `{ amount }` → same (not query)
- `POST /auctions/{id}/watch` → same
- `DELETE /auctions/{id}/watch` → same
- `GET /artworks/{id}/offers` → `GET /offers/artwork/{id}`
- `POST /offers` → `POST /offers` `{ artworkId, amount }`
- `GET /offers/pending` → `GET /offers/pending`

**message / review / cms / competition / event / user / report / standard / bid**

- `GET /messages/threads` → `GET /messages/threads`
- `GET /messages/threads/{id}` → same
- `POST /messages/threads/{id}` `{ body }` → same
- `GET /artworks/{id}/reviews` → `GET /artworks/{id}/reviews`
- `POST /reviews` → `POST /artworks/{id}/reviews`
- `GET /cms/config` → `GET /cms/config`
- `PUT /cms/config` → `PUT /cms/config`
- `POST /contact` → `POST /contact`
- `GET /competitions` → `GET /competitions`
- `POST /competitions` → `POST /competitions`
- `POST /competitions/{id}/register` → `POST /competitors/register`
- `POST /competitions/{id}/vote` → `POST /competitors/vote`
- `GET /competition-competitor-data` → `GET /competitors`
- `GET /events` → `GET /events`
- `POST /events` → `POST /events`
- `PUT /events/{id}` → `PATCH /events/{id}`
- `GET /events/pending` → `GET /events?status=PENDING` (manager)
- `PUT /events/{id}/accept|reject` → `PATCH /events/change/status/{id}`
- `GET /events/{id}/image` → **removed**; `imageUrl`
- `GET /user/profile` → `GET /users/me`
- `PUT /user/profile` → `PATCH /users/me`
- `GET /user/notifications` → `GET /users/me/notifications`
- `PUT /user/notifications/{id}/read` → `PATCH /users/me/notifications/{id}/read`
- `GET /users/all` → `GET /admin/users/all`
- `DELETE /users/{id}` → `DELETE /users/{id}`
- `GET /users/{role}-list` → `GET /admin/users/role?uuid=ROLE_{role}`
- `DELETE /users` `{ ids }` → **not in 1.0** (loop `DELETE /users/{id}`)
- `POST /users/profile/upload` multipart → `POST /users/me/photo`
- `GET /users/profile/photo` → use `avatarUrl` on profile
- `POST /notifications/send` → out of 1.0 (manager email stays internal)
- `GET /moderation/queue` → `GET /moderation/queue`
- `PUT /moderation/{id}/approve|reject` → same
- `POST /report/create` → `POST /report`
- `GET /report/all` → `GET /report/all`
- `GET /standards` → `GET /standards`
- `POST /standard/add` → `POST /standards`
- `DELETE /standards/{id}` → `DELETE /standards/{id}`
- `POST /bid/saveBidArt` → **removed**; use `POST /auctions/{id}/bid`

**Signature unification (mock and HTTP must match)**

- `user.getProfile()` / `user.updateProfile(data)` — no `userId` argument
- `auction.placeBid(auctionId, amount)`
- `message.send(threadId, body)`
- `offer.getPendingForArtist()`
- `order.getAll()` — server filters by JWT

### Backend controllers today → canonical

- `AccountController` `/api/v1/auth/login` → keep; drop `UserType.CUSTOMER` restriction
- `AccountController` `/auth/customer/signup` → alias of `/auth/register`
- `RefreshTokenController` `/auth/token/refresh` → keep
- `SecurityConfig` logout `/api/v1/logout` → alias of `/auth/logout`
- `AdminAccountController` `/admin/auth/**` → aliases of `/auth/**`
- `ArtworkController` `/user/artwork/**` → aliases of `/artworks/**`
- `AdminArtworkController` `/admin/artwork/**` → keep for admin; add `/moderation/**` aliases
- `ArtistProfileController` `/artists/{id}` → also resolve `{slug}`; make GET public
- `DiscoveryController` `/discovery/collections` → alias of `/collections`
- `DiscoveryController` `/discovery/trending` → alias of `/artworks/trending`
- `CartController` `POST /cart/add` → alias of `POST /cart`
- `CartController` `DELETE /cart/clear` → alias of `DELETE /cart`
- `WishListController` `POST /wishlist/save/{id}` → alias of `POST /wishlist/{id}`
- `OrderController` `GET /orders/my` → alias of `GET /orders`
- `OrderController` `POST /orders` → unpaid draft only; document as non-checkout
- `CheckoutController` `POST /checkout` → **canonical paid path**
- `ChapaController` `GET /chapa/callback` → keep public
- `AuctionController` → public GETs; JSON bid body
- `OfferController` → JSON create; artist may PATCH status
- `PayoutController` → JSON `{ amount }` on request
- `ShipmentController` → JSON ship body
- `RatingController` → JSON rate body; public GETs
- `EventController` → public list of ACCEPTED; `POST /events`
- `CompetitionController` `/competition/**` → aliases of `/competitions/**`
- `CompetitorController` → vote JSON; fix `hasRole`
- `StandardController` `/standards/add` → alias of `POST /standards`
- `ReportController` `/report/create` → alias of `POST /report`
- `UserController` photo query-string → `POST /users/me/photo`
- `AdminController` / `AdminDashboardController` / `AdminPermissionController` → keep
- **Missing controllers to add:** messages, collections-by-slug, certificates, cms, contact, `GET /users/me`, `GET /orders/{id}`

---

## G. Compatibility matrix

Live mode (`VUE_APP_USE_MOCK=false`) vs this contract. Mock mode may skip OTP/Chapa/STOMP.

| Check | Mock | Live (contract 1.0) |
| --- | --- | --- |
| Axios `baseURL` ends with `/api/v1` on port 8088 | n/a | required |
| Unwrap `response.data.content` (and `pageable`) | mock returns unwrapped | required |
| Login with `username` + `password` + `channel` | mock token | JWT `UserInfo` |
| OTP step when login `message` mentions Otp | skip | `POST /auth/login/verify` |
| Role from JWT `role` (strip `ROLE_`) | mock-jwt map | JWT claim + `UserInfo.role` |
| Public gallery `/`, `/allArtwork`, `/artworks/:id` without token | mock data | `GET /artworks` public |
| Artist page `/artists/:slug` | hardcoded slug map | `GET /artists/{slug}` |
| Images from `imageUrls` on origin 8088 | mock URLs | `{origin}/upload/images/…` |
| Cart add JSON `{ artworkId, quantity }` | mock | merge lines, stock check |
| Checkout | mock place-order | `POST /checkout` → redirect `checkOutUrl` (Chapa) |
| PayPal SDK | hidden in mock | **must not** run |
| Orders list `/account/orders` | mock | `GET /orders` |
| Wishlist many items | mock | unique `(user, artwork)` |
| Collections `/collections/:slug` | mock | `GET /collections/{slug}` |
| Auction list public; bid JSON `{ amount }` | mock | public GET; JWT bid; 409 if stale |
| Messages inbox | mock | `GET /messages/threads` |
| Reviews on artwork detail | mock | `GET/POST /artworks/{id}/reviews` |
| STOMP `/ws/notifications` | disabled | SockJS + Bearer |
| CORS origin `http://localhost:8080` | n/a | required in dev |
| MANAGER / ORGANIZATION dashboards | mock users | seeded roles + permissions |

---

## Process

1. This file is frozen as **v1.0**.
2. Backend and frontend PRs must name the section they implement (B–E).
3. New endpoints require a contract bump (1.1) in this header.
4. After controllers match, export `/v3/api-docs` and diff tags/paths against sections C–E.

Frontend unwrap rules and env: [`vue-oag-frontend/docs/api-contract.md`](../../vue-oag-frontend/docs/api-contract.md).
