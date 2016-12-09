package com.aaron.android.codelibrary.http;


import org.apache.http.HttpStatus;

import java.util.Collections;

/**
 * Created on 16/1/27.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class NetworkErrorResponse {

    /** The HTTP status code. */
    public final int statusCode;

    /** Raw data from this response. */
    public final byte[] data;

    /** Response headers. */
    public final Object headers;

    /** True if the server returned a 304 (Not Modified). */
    public final boolean notModified;

    /** Network roundtrip time in milliseconds. */
    public final long networkTimeMs;

    /**
     * Creates a new network response.
     * @param statusCode the HTTP status code
     * @param data Response body
     * @param headers Headers returned with this response, or null for none
     * @param notModified True if the server returned a 304 and the data was already in cache
     * @param networkTimeMs Round-trip network time to receive network response
     */
    public NetworkErrorResponse(int statusCode, byte[] data, Object headers,
                                boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    public NetworkErrorResponse(int statusCode, byte[] data, Object headers,
                                boolean notModified) {
        this(statusCode, data, headers, notModified, 0);
    }

    public NetworkErrorResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false, 0);
    }

    public NetworkErrorResponse(byte[] data, Object headers) {
        this(HttpStatus.SC_OK, data, headers, false, 0);
    }

}