[![Build Status](https://travis-ci.org/jdesrosiers/http-server.svg?branch=master)](https://travis-ci.org/jdesrosiers/http-server)

# http-server
This project has two main parts.  The first is an HTTP Server micro-framework called Flint.  The second is a server implementation using Flint that implements the [cob_spec](https://github.com/jdesrosiers/cob_spec) specification.

# Development
All code is written in Java 8 and uses the Gradle build tool

## Test
Tests use JUnit with the [junit-dataprovider](https://github.com/TNG/junit-dataprovider) test runner.  Tests can be run through gradle.

```
> ./gradlew test
```

The cob_spec part of this project has [FitNesse tests](https://github.com/jdesrosiers/cob_spec).  You can refer to that project to see how to run those tests.

## Run
You can run the server with gradle

```
> ./gradlew run
```

### Logs
Logs are written to stdout.  Cob_spec expects to find logs in the /logs file.  You can have logs write to the log file from the command line.

```
> ./gradlew run > public/logs
```

Or, if you want to see the logs *and* write them to file you can use `tee`.

```
> ./gradlew run | tee public/logs
```

### Arguments
The server takes two optional arguments `[-p PORT] [-d DIRECTORY]`.  Where `PORT` is the port the server will listen on and `DIRECTORY` is where many of the resources expected in cob_spec will be served from.  You can pass these arguments to the gradle `run` command with the `appArgs` argument.

**Defaults:**
* PORT: 5000
* DIRECTORY: public

```
> ./gradlew run -PappArgs="-p 5000 -d public"
```

## Dependencies

### Javaslang
[Javaslang](http://www.javaslang.io/) is a functional programming library that makes using functional patterns in Java a little nicer.  This project prefers Javaslang data structures over Java data structures because they are immutable and because they allow use of Javaslang functional programming features that don't exist in Java.

### JParsec
[JParsec](https://github.com/jparsec/jparsec) is a parser combinator that is used to parse HTTP messages.  Using something like this allows me to implement a parser in a syntax that is very similar to the ABNF syntax used in all the RFCs that define HTTP and other supporting specifications.  This way I can be more confident that I am implementing the specification correctly and completely.

# Flint
Flint is largely inspired by my favorite web micro-framework [Silex](https://silex.sensiolabs.org/), which in turn is inspired by [Sinatra](http://www.sinatrarb.com/).

## Routes
The `Application` class represents your application.  You define your application by adding routes that it serves.

```java
Application app = new Application();

app.get("/hello", request -> {
    Response response = Response.create(StatusCode.OK);
    response.setHeader("Content-Type", "text/plain; charset=utf-8");
    response.setBody("world");

    return response;
});
```

You are free to define your controllers any way you like as long as they take a `Request` and return a `Response`.

```java
MyController myController = new MyController();
app.get("/foo/bar", myController::get);
app.post("/foo", myController::post);
app.delete("/foo/bar", myController::delete);
```

Route createion helper functions exist for the following HTTP methods: GET, POST, PUT, PATCH, DELETE, OPTIONS.  If you want to use another method you can use `Application::match` to specify your method.

```java
app.match("FOO", "/hello", request -> /* ... */);
```

## Requests
The `Request` class represents an HTTP request message.  It gives you access to the request's method, path, headers, and body.  Headers are always returned as `Option`s.

```java
request.getHeader("Content-Type");
request.getBody();
```

HTTP defines several request formats.  This implementation supports only the most common way called "origin-form".  This form expects the domain to be specified in the `Host` header and the request-target to be the absolute-path with resepect the the `Host` domain.

## Response
The `Response` class represents an HTTP response message.  It allows you to set the status code, headers, and body of a response.  Create responses with the `Response::create` factory method.  The `StatusCode` class contains constants for all the standard status codes.

The `Content-Length` header is set automatically when you set the body of a response.

```java
Response response = Response.create(StatusCode.OK);
response.setBody("foo");
response.getHeader("Content-Length"); // Some("3")
response.setBody("foobar");
response.getHeader("Content-Length"); // Some("6")
```

The body can also be set as an `InputStream` to avoid reading large resources into memory.
```java
response.setBody(Files.newInputStream(path);
```

## MediaType
The `MediaType` class maps file extensions to content types.

```java
MediaType.fromExtension("txt"); // Some("text/plain; charset=utf-8")
```

## FormUrlencoded
The `FormUrlencoded` class allows you read, manipulate, and write content in the `application/x-www-form-urlencoded` format.  This is the format HTML Forms use by default and the format normally used for query params.

```java
Option<FormUrlencoded> content = request.getBody().map(FormUrlencoded::new);
Option<FormUrlencoded> queryParams = request.getQuery();
String pageSize = queryParams
    .flatMap(query -> query.get("pageSize")
    .getOrElse("10");
```

## Cookie
The `Cookie` class provides support for the `Cookie` and `Set-Cookie` headers.  Cookie support is very minimal.  It allows reading and writing key value pairs.

```java
Option<Cookie> cookie = request.getHeader("Cookie").map(Cookie::new);
String fooCookie = cookie
    .flatMap(c -> c.get("foo")
    .getOrElse("42");
```

## UnixPatch

# Project Plan

## Current Sprint
**From** 2017-05-01 **to** 2017-05-05

| AV  | IP  | CP  | SP  | Description |
|:---:|:---:|:---:|:---:|-------------|
|     |     |  X  |  3  | Fill out README file.  This should include documentation for developing, testing, and running the application.  It should document the Flint micro-framework.  It should include a project planning section to track progress and elicit feedback.
|     |     |  X  |  1  | Rename the micro-framework from core to flint.
|     |     |  X  |  1  | Rename HttpServer to CobSpec to clearly indicate that it is the implementation of cob_spec.
|     |     |  X  |  2  | Move FileSystemControllerTest fixtures to test resources.  This should be trivial, but doing this in Java is always harder than it seems.
|  X  |     |     |  1  | Move Response writing out of `Application`.  `Server` is probably the right place, but I haven't decided for sure.
|     |     |  X  |  1  | Refactor parsers for consistent style.  As I worked on these, the style I chose to use has evolved.  I need to go back and make them consistent.
|     |     |  X  |  1  | Convert StatusCode tests to parameterized tests
|     |     |  X  |  1  | Audit the code for `*` imports and clean up where necessary.
|     |     |  X  |  1  | Audit the code for unused imports and clean up where necessary.
|     |  X  |     |  5  | Singe range partial content support.  It should ignore ranges it doesn't understand.  It should pass the ResponseTestSuite.PartialContent cob_spec test.
|  X  |     |     |  2  | Decode URL encoded characters.  It should pass the ResponseTestSuite.ParameterDecode cob_spec test.

**Legend:** *AV* => Available, *IP* => In Progress, *CP* => Completed, *SP* => Story Points

## Backlog
| SP  | Description |
|:---:|-------------|
|  3  | Add support for before and after middleware.  This enhances the flexibility of the micro-framework and can improve implementation of several features including logging and partial content.
|  3  | Add support for URI Templates.  This would allow routes like `app.get("/foo/{id}", request -> ...)`.  Currently routes can only do exact matches which limits how dynamic the server can be.
|  1  | Logging as middleware.  Currently logging is done automatically.  If it is done a middleware instead it gives the developer control over how he wants to do logging.
|  2  | Multiple byte range partial content support.  Once single byte range support is complete, this shouldn't be too difficult.
|  2  | Decouple parser from flint and make it its own package.  If the dependency only goes one way, it could later be expanded into a general purpose tool for parsing anything HTTP related.  For example, an HTTP Client would require a parser that has a lot of overlap with the parsing needs for an HTTP Server.

