package com.project.oag.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";
    private static final String CONTRACT_VERSION = "1.0";

    @Bean
    public OpenAPI openAPI() {
        String description = """
                Frozen KELEM API contract v%s.

                Prefix `/api/v1` on port 8088. Envelope `{ status, message, content }` (pageable adds `pageable`).
                Payments: Chapa/ETB via POST `/api/v1/checkout`. Realtime: STOMP `/ws/notifications`.

                Human contract (source of truth): `docs/api-contract.md` in this repository.
                Vue unwrap rules: `vue-oag-frontend/docs/api-contract.md`.

                Resource list (same as the markdown contract):
                Auth, Users, Artworks, Artists, Collections, Cart, Wishlist, Checkout, Orders,
                Offers, Auctions, Payouts, Shipments, Certificates, Messages, Reviews,
                Events, Competitions, Standards, Reports, CMS, Admin, Moderation.
                """.formatted(CONTRACT_VERSION);

        return new OpenAPI()
                .info(new Info()
                        .title("KELEM Online Art Gallery API")
                        .description(description)
                        .version(CONTRACT_VERSION)
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("KELEM API Contract v%s (frozen) — docs/api-contract.md".formatted(CONTRACT_VERSION))
                        .url("/v3/api-docs"))
                .servers(List.of(
                        new Server().url("http://localhost:8088").description("Local (contract v1.0)"),
                        new Server().url("/").description("Current host")
                ))
                .tags(List.of(
                        tag("Auth", "B. Login, register, OTP, password, refresh, logout"),
                        tag("Users", "B. Profile /users/me, admin user admin"),
                        tag("Artworks", "C. Public catalog and artist submit"),
                        tag("Artists", "C. Public artist profiles, follow"),
                        tag("Collections", "C. Curated collections by slug"),
                        tag("Cart", "D. Cart merge and totals"),
                        tag("Wishlist", "D. Many items per user"),
                        tag("Checkout", "D. Chapa POST /checkout"),
                        tag("Orders", "D. Buyer and admin orders"),
                        tag("Offers", "D. Make and accept offers"),
                        tag("Auctions", "D. Public list, JSON bids"),
                        tag("Payouts", "D. Artist wallet and admin approve"),
                        tag("Shipments", "D. Tracking"),
                        tag("Certificates", "D. CoA verify"),
                        tag("Messages", "E. Collector–artist threads"),
                        tag("Reviews", "E. Ratings and text reviews"),
                        tag("Events", "E. Public accepted events, tickets"),
                        tag("Competitions", "E. Public list, register, vote"),
                        tag("Standards", "E. Platform standards"),
                        tag("Reports", "E. User reports"),
                        tag("CMS", "E. Config and contact"),
                        tag("Admin", "E. KPIs, permissions, register"),
                        tag("Moderation", "E. Artwork approve/reject aliases")
                ))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name(BEARER_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    private static Tag tag(String name, String description) {
        return new Tag().name(name).description(description);
    }
}
