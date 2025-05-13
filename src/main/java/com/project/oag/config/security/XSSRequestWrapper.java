package com.project.oag.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.oag.exceptions.XSSServletException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.owasp.encoder.Encode;

import java.io.*;
import java.util.List;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

        @Getter
        private final String body;
        private final List<String> skipWords;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XSSRequestWrapper(HttpServletRequest request, String body, List<String> skipWords) {
        super(request);
        this.body = body;
        this.skipWords = skipWords;
    }

    public XSSRequestWrapper(HttpServletRequest request, List<String> skipWords) throws IOException {
        super(request);
        this.skipWords = skipWords;

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try (InputStream inputStream = request.getInputStream()) {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
    }

    private boolean sanitize(String input) {
        if (!XSSValidationUtils.isValidURL(input, skipWords)) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "XSS attack error");
            try {
                String response = XSSValidationUtils.convertObjectToJson(errorResponse);
                throw new XSSServletException(response);
            } catch (JsonProcessingException e) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String getParameter(String paramName) {
        String value = super.getParameter(paramName);
        sanitize(value);
        return Encode.forHtml(value);
    }

    @Override
    public String[] getParameterValues(String paramName) {
        String[] values = super.getParameterValues(paramName);
        if (values != null) {
            for (int index = 0; index < values.length; index++) {
                sanitize(values[index]);
                values[index] = Encode.forHtml(values[index]);
            }
        }
        return values;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No implementation needed
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}
