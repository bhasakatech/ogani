package com.bhasaka.ogani.core.services.Impl;

import com.bhasaka.ogani.core.config.QuoteServiceConfiguration;
import com.bhasaka.ogani.core.services.QuoteService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * OSGi service implementation that fetches quote data from a configured API endpoint.
 */
@Component(immediate = true, service = QuoteService.class)
@Designate(ocd = QuoteServiceConfiguration.class)
public class QuoteServiceImpl implements QuoteService {
    
    Logger log = LoggerFactory.getLogger(QuoteService.class);

    private final HttpClient client = HttpClient.newHttpClient();

    private QuoteServiceConfiguration config;


    /**
     * Activates or reconfigures the service with the latest OSGi configuration.
     *
     * @param config the quote service configuration
     */
    @Activate
    @Modified
    public void activate(QuoteServiceConfiguration config ) {
        this.config = config;
        log.info("Quote service modified");
        getResponse();
    }

    /**
     * Calls the configured quote API and returns the raw JSON response.
     *
     * @return quote API response body, or {@code null} when disabled or on error
     */
    @Override
    public String getResponse(){
        String jsonResponse = null;

        try {
            if (config == null || !config.enabled()) {
                return null;
            }

            final String url = "https://" + config.apiURL().trim();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            log.info(url);

            int statusCode = response.statusCode();
            log.info("Status Code: {}", statusCode);

            jsonResponse = response.body();
        } catch (NullPointerException e) {
            log.error("Configuration is incomplete: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid quote API URL: {}", e.getMessage());
        } catch (IOException e) {
            log.error("I/O error while calling quote API: {}", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Quote API call interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception while fetching quote: {}", e.getMessage());
        }

        return jsonResponse;
    }

}

