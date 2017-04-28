package org.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import javaslang.control.Option;

@RunWith(DataProviderRunner.class)
public class StatusCodeTest {

    @Test
    public void continueShouldBe100() {
        assertThat(StatusCode.CONTINUE, equalTo(100));
    }

    @Test
    public void continueShouldBe101() {
        assertThat(StatusCode.SWITCHING_PROTOCOLS, equalTo(101));
    }

    @Test
    public void oKShouldBe200() {
        assertThat(StatusCode.OK, equalTo(200));
    }

    @Test
    public void createdShouldBe201() {
        assertThat(StatusCode.CREATED, equalTo(201));
    }

    @Test
    public void acceptedShouldBe202() {
        assertThat(StatusCode.ACCEPTED, equalTo(202));
    }

    @Test
    public void nonAuthoritativeInformationShouldBe203() {
        assertThat(StatusCode.NON_AUTHORITATIVE_INFORMATION, equalTo(203));
    }

    @Test
    public void noContentShouldBe204() {
        assertThat(StatusCode.NO_CONTENT, equalTo(204));
    }

    @Test
    public void resetContentShouldBe205() {
        assertThat(StatusCode.RESET_CONTENT, equalTo(205));
    }

    @Test
    public void partialContentShouldBe206() {
        assertThat(StatusCode.PARTIAL_CONTENT, equalTo(206));
    }

    @Test
    public void multipleChoicesShouldBe300() {
        assertThat(StatusCode.MULTIPLE_CHOICES, equalTo(300));
    }

    @Test
    public void movedPermanentlyShouldBe301() {
        assertThat(StatusCode.MOVED_PERMANENTLY, equalTo(301));
    }

    @Test
    public void foundShouldBe302() {
        assertThat(StatusCode.FOUND, equalTo(302));
    }

    @Test
    public void seeOtherShouldBe303() {
        assertThat(StatusCode.SEE_OTHER, equalTo(303));
    }

    @Test
    public void notModifiedShouldBe304() {
        assertThat(StatusCode.NOT_MODIFIED, equalTo(304));
    }

    @Test
    public void useProxyShouldBe305() {
        assertThat(StatusCode.USE_PROXY, equalTo(305));
    }

    @Test
    public void temporaryRedirectShouldBe307() {
        assertThat(StatusCode.TEMPORARY_REDIRECT, equalTo(307));
    }

    @Test
    public void badRequestShouldBe400() {
        assertThat(StatusCode.BAD_REQUEST, equalTo(400));
    }

    @Test
    public void unauthorizedShouldBe401() {
        assertThat(StatusCode.UNAUTHORIZED, equalTo(401));
    }

    @Test
    public void paymentRequiredShouldBe402() {
        assertThat(StatusCode.PAYMENT_REQUIRED, equalTo(402));
    }

    @Test
    public void forbiddenShouldBe403() {
        assertThat(StatusCode.FORBIDDEN, equalTo(403));
    }

    @Test
    public void notFoundShouldBe404() {
        assertThat(StatusCode.NOT_FOUND, equalTo(404));
    }

    @Test
    public void methodNotAllowedShouldBe405() {
        assertThat(StatusCode.METHOD_NOT_ALLOWED, equalTo(405));
    }

    @Test
    public void notAcceptableShouldBe406() {
        assertThat(StatusCode.NOT_ACCEPTABLE, equalTo(406));
    }

    @Test
    public void proxyAuthenticationRequiredShouldBe407() {
        assertThat(StatusCode.PROXY_AUTHENTICATION_REQUIRED, equalTo(407));
    }

    @Test
    public void requestTimeoutShouldBe408() {
        assertThat(StatusCode.REQUEST_TIMEOUT, equalTo(408));
    }

    @Test
    public void conflictShouldBe409() {
        assertThat(StatusCode.CONFLICT, equalTo(409));
    }

    @Test
    public void goneShouldBe410() {
        assertThat(StatusCode.GONE, equalTo(410));
    }

    @Test
    public void lengthRequiredShouldBe411() {
        assertThat(StatusCode.LENGTH_REQUIRED, equalTo(411));
    }

    @Test
    public void preconditionFailedShouldBe412() {
        assertThat(StatusCode.PRECONDITION_FAILED, equalTo(412));
    }

    @Test
    public void payloadTooLargeShouldBe413() {
        assertThat(StatusCode.PAYLOAD_TOO_LARGE, equalTo(413));
    }

    @Test
    public void uriTooLongShouldBe414() {
        assertThat(StatusCode.URI_TOO_LONG, equalTo(414));
    }

    @Test
    public void unsupportedMediaTypeShouldBe415() {
        assertThat(StatusCode.UNSUPPORTED_MEDIA_TYPE, equalTo(415));
    }

    @Test
    public void rangeNotSatisfiableShouldBe416() {
        assertThat(StatusCode.RANGE_NOT_SATISFIABLE, equalTo(416));
    }

    @Test
    public void expectationFailedShouldBe417() {
        assertThat(StatusCode.EXPECTATION_FAILED, equalTo(417));
    }

    @Test
    public void ImATeapotShouldBe418() {
        assertThat(StatusCode.IM_A_TEAPOT, equalTo(418));
    }

    @Test
    public void UnprocessableEntityShouldBe422() {
        assertThat(StatusCode.UNPROCESSABLE_ENTITY, equalTo(422));
    }

    @Test
    public void upgradRequiredShouldBe426() {
        assertThat(StatusCode.UPGRADE_REQUIRED, equalTo(426));
    }

    @Test
    public void internalServerErrorShouldBe500() {
        assertThat(StatusCode.INTERNAL_SERVER_ERROR, equalTo(500));
    }

    @Test
    public void notImplementedShouldBe501() {
        assertThat(StatusCode.NOT_IMPLEMENTED, equalTo(501));
    }

    @Test
    public void badGatewayShouldBe502() {
        assertThat(StatusCode.BAD_GATEWAY, equalTo(502));
    }

    @Test
    public void serviceUnavailableShouldBe503() {
        assertThat(StatusCode.SERVICE_UNAVAILABLE, equalTo(503));
    }

    @Test
    public void gatewayTimeoutShouldBe504() {
        assertThat(StatusCode.GATEWAY_TIMEOUT, equalTo(504));
    }

    @Test
    public void httpVersionNotSupportedShouldBe505() {
        assertThat(StatusCode.HTTP_VERSION_NOT_SUPPORTED, equalTo(505));
    }

    @Test
    public void _100ShouldBeContinue() {
        assertThat(StatusCode.getMessage(StatusCode.CONTINUE), equalTo(Option.of("Continue")));
    }

    @Test
    public void _101ShouldBeSwitchingProtocols() {
        assertThat(StatusCode.getMessage(StatusCode.SWITCHING_PROTOCOLS), equalTo(Option.of("Switching Protocols")));
    }

    @Test
    public void _200ShouldBeOK() {
        assertThat(StatusCode.getMessage(StatusCode.OK), equalTo(Option.of("OK")));
    }

    @Test
    public void _201ShouldBeCreated() {
        assertThat(StatusCode.getMessage(StatusCode.CREATED), equalTo(Option.of("Created")));
    }

    @Test
    public void _202ShouldBeAccepted() {
        assertThat(StatusCode.getMessage(StatusCode.ACCEPTED), equalTo(Option.of("Accepted")));
    }

    @Test
    public void _203ShouldBeNonAuthoritativeInformation() {
        assertThat(StatusCode.getMessage(StatusCode.NON_AUTHORITATIVE_INFORMATION), equalTo(Option.of("Non-Authoritative Information")));
    }

    @Test
    public void _204ShouldBeNoContent() {
        assertThat(StatusCode.getMessage(StatusCode.NO_CONTENT), equalTo(Option.of("No Content")));
    }

    @Test
    public void _205ShouldBeResetContent() {
        assertThat(StatusCode.getMessage(StatusCode.RESET_CONTENT), equalTo(Option.of("Reset Content")));
    }

    @Test
    public void _206ShouldBePartialContent() {
        assertThat(StatusCode.getMessage(StatusCode.PARTIAL_CONTENT), equalTo(Option.of("Partial Content")));
    }

    @Test
    public void _300ShouldBeMultipleChoices() {
        assertThat(StatusCode.getMessage(StatusCode.MULTIPLE_CHOICES), equalTo(Option.of("Multiple Choices")));
    }

    @Test
    public void _301ShouldBeMovedPermanently() {
        assertThat(StatusCode.getMessage(StatusCode.MOVED_PERMANENTLY), equalTo(Option.of("Moved Permanently")));
    }

    @Test
    public void _302ShouldBeFound() {
        assertThat(StatusCode.getMessage(StatusCode.FOUND), equalTo(Option.of("Found")));
    }

    @Test
    public void _303ShouldBeSeeOther() {
        assertThat(StatusCode.getMessage(StatusCode.SEE_OTHER), equalTo(Option.of("See Other")));
    }

    @Test
    public void _304ShouldBeNotModified() {
        assertThat(StatusCode.getMessage(StatusCode.NOT_MODIFIED), equalTo(Option.of("Not Modified")));
    }

    @Test
    public void _305ShouldBeUseProxy() {
        assertThat(StatusCode.getMessage(StatusCode.USE_PROXY), equalTo(Option.of("Use Proxy")));
    }

    @Test
    public void _307ShouldBeTemporaryRedirect() {
        assertThat(StatusCode.getMessage(StatusCode.TEMPORARY_REDIRECT), equalTo(Option.of("Temporary Redirect")));
    }

    @Test
    public void _400ShouldBeBadRequest() {
        assertThat(StatusCode.getMessage(StatusCode.BAD_REQUEST), equalTo(Option.of("Bad Request")));
    }

    @Test
    public void _401ShouldBeUnathorized() {
        assertThat(StatusCode.getMessage(StatusCode.UNAUTHORIZED), equalTo(Option.of("Unauthorized")));
    }

    @Test
    public void _402ShouldBePaymentRequired() {
        assertThat(StatusCode.getMessage(StatusCode.PAYMENT_REQUIRED), equalTo(Option.of("Payment Required")));
    }

    @Test
    public void _403ShouldBeFobidden() {
        assertThat(StatusCode.getMessage(StatusCode.FORBIDDEN), equalTo(Option.of("Forbidden")));
    }

    @Test
    public void _404ShouldBeNotFound() {
        assertThat(StatusCode.getMessage(StatusCode.NOT_FOUND), equalTo(Option.of("Not Found")));
    }

    @Test
    public void _405ShouldBeMethodNotAllowed() {
        assertThat(StatusCode.getMessage(StatusCode.METHOD_NOT_ALLOWED), equalTo(Option.of("Method Not Allowed")));
    }

    @Test
    public void _406ShouldBeNotAcceptable() {
        assertThat(StatusCode.getMessage(StatusCode.NOT_ACCEPTABLE), equalTo(Option.of("Not Acceptable")));
    }

    @Test
    public void _407ShouldBeProxyAuthenticationRequired() {
        assertThat(StatusCode.getMessage(StatusCode.PROXY_AUTHENTICATION_REQUIRED), equalTo(Option.of("Proxy Authentication Required")));
    }

    @Test
    public void _408ShouldBeRequestTimeout() {
        assertThat(StatusCode.getMessage(StatusCode.REQUEST_TIMEOUT), equalTo(Option.of("Request Timeout")));
    }

    @Test
    public void _409ShouldBeConflict() {
        assertThat(StatusCode.getMessage(StatusCode.CONFLICT), equalTo(Option.of("Conflict")));
    }

    @Test
    public void _410ShouldBeGone() {
        assertThat(StatusCode.getMessage(StatusCode.GONE), equalTo(Option.of("Gone")));
    }

    @Test
    public void _411ShouldBeLengthRequired() {
        assertThat(StatusCode.getMessage(StatusCode.LENGTH_REQUIRED), equalTo(Option.of("Length Required")));
    }

    @Test
    public void _412ShouldBePreconditionFailded() {
        assertThat(StatusCode.getMessage(StatusCode.PRECONDITION_FAILED), equalTo(Option.of("Precondition Failed")));
    }

    @Test
    public void _413ShouldBePayloadTooLarge() {
        assertThat(StatusCode.getMessage(StatusCode.PAYLOAD_TOO_LARGE), equalTo(Option.of("Payload Too Large")));
    }

    @Test
    public void _414ShouldBeUriTooLong() {
        assertThat(StatusCode.getMessage(StatusCode.URI_TOO_LONG), equalTo(Option.of("URI Too Long")));
    }

    @Test
    public void _415ShouldBeUnsupportedMediaType() {
        assertThat(StatusCode.getMessage(StatusCode.UNSUPPORTED_MEDIA_TYPE), equalTo(Option.of("Unsupported Media Type")));
    }

    @Test
    public void _416ShouldBeRangeNotSatisfiable() {
        assertThat(StatusCode.getMessage(StatusCode.RANGE_NOT_SATISFIABLE), equalTo(Option.of("Range Not Satisfiable")));
    }

    @Test
    public void _417ShouldBeExpectationFailed() {
        assertThat(StatusCode.getMessage(StatusCode.EXPECTATION_FAILED), equalTo(Option.of("Expectation Failed")));
    }

    @Test
    public void _418ShouldBeImATeapot() {
        assertThat(StatusCode.getMessage(StatusCode.IM_A_TEAPOT), equalTo(Option.of("I'm a teapot")));
    }

    @Test
    public void _422ShouldBeUnprocessableEntity() {
        assertThat(StatusCode.getMessage(StatusCode.UNPROCESSABLE_ENTITY), equalTo(Option.of("Unprocessable Entity")));
    }

    @Test
    public void _426ShouldBeUpgradRequired() {
        assertThat(StatusCode.getMessage(StatusCode.UPGRADE_REQUIRED), equalTo(Option.of("Upgrade Required")));
    }

    @Test
    public void _500ShouldBeInternalServerError() {
        assertThat(StatusCode.getMessage(StatusCode.INTERNAL_SERVER_ERROR), equalTo(Option.of("Internal Server Error")));
    }

    @Test
    public void _501ShouldBeNotImplemented() {
        assertThat(StatusCode.getMessage(StatusCode.NOT_IMPLEMENTED), equalTo(Option.of("Not Implemented")));
    }

    @Test
    public void _502ShouldBeBadGateway() {
        assertThat(StatusCode.getMessage(StatusCode.BAD_GATEWAY), equalTo(Option.of("Bad Gateway")));
    }

    @Test
    public void _503ShouldBeServiceUnavailable() {
        assertThat(StatusCode.getMessage(StatusCode.SERVICE_UNAVAILABLE), equalTo(Option.of("Service Unavailable")));
    }

    @Test
    public void _504ShouldBeGatewayTimeout() {
        assertThat(StatusCode.getMessage(StatusCode.GATEWAY_TIMEOUT), equalTo(Option.of("Gateway Timeout")));
    }

    @Test
    public void _505ShouldBeHttpVersionNotSupported() {
        assertThat(StatusCode.getMessage(StatusCode.HTTP_VERSION_NOT_SUPPORTED), equalTo(Option.of("HTTP Version Not Supported")));
    }

}
