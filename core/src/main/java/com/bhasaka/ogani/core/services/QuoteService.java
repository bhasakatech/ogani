package com.bhasaka.ogani.core.services;


/**
 * Service contract for retrieving quote payload data.
 */
public interface QuoteService {


    /**
     * Fetches and returns the quote payload.
     *
     * @return quote response as a JSON string, or {@code null} when unavailable
     */
    String getResponse();
}
