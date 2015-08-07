/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.exception;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a genera exception thrown by the StreamClient.
 */
public abstract class StreamClientException extends Exception {

    private int httpStatusCode;
    private int code;
    private HashMap<String,ArrayList<HashMap<String,ArrayList<String>>>> exceptionFields;
    private String detail;
    private String duration;
    private String exception;

    public StreamClientException() {
        super();
    }

    public StreamClientException(String message) {
        super(message);
    }

    public StreamClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamClientException(Throwable cause) {
        super(cause);
    }

    protected StreamClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HashMap<String,ArrayList<HashMap<String,ArrayList<String>>>> getExceptionFields() {
        return exceptionFields;
    }

    public void setExceptionFields(HashMap<String,ArrayList<HashMap<String,ArrayList<String>>>> exceptionFields) {
        this.exceptionFields = exceptionFields;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public String getException() {return exception;}

    public void setException(String exception) { this.exception = exception; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                       .add("status_code", this.httpStatusCode)
                       .add("code", this.code)
                       .add("exception", this.exception)
                       .add("detail", this.detail)
                       .add("exception_fields",this.exceptionFields)
                       .add("duration",this.duration).toString();
    }
}
