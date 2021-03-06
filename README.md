[![Build Status](https://travis-ci.org/jdesrosiers/http-server.svg?branch=master)](https://travis-ci.org/jdesrosiers/http-server)

# http-server
This project has two main parts.  The first is an HTTP Server micro-framework called Flint.  The second is a server implementation using Flint that implements the [cob_spec](https://github.com/jdesrosiers/cob_spec) specification.

# Development
All code is written in Java 8 and uses the Activator build tool

## Build
You can assemble the server into a jar using the following command.  The result can be found at `target/scala-2.11/http-server-assembly-1.0.jar`

```
> bin/activator assembly
```

## Test
Tests use JUnit with the [junit-dataprovider](https://github.com/TNG/junit-dataprovider) test runner.  Tests can be run through activator.

```
> bin/activator test
```

The cob_spec part of this project has [FitNesse tests](https://github.com/jdesrosiers/cob_spec).  You can refer to that project to see how to run those tests.

## Run
You can run the server with activator

```
> bin/activator run
```

Or, run the assembled jar

```
> java -jar target/scala-2.11/http-server-assembly.jar
```

### Arguments
The server takes two optional arguments `[-p PORT] [-d DIRECTORY]`.  Where `PORT` is the port the server will listen on and `DIRECTORY` is where many of the resources expected in cob_spec will be served from.

**Defaults:**
* PORT: 5000
* DIRECTORY: public

```
> bin/activator "run -p 5000 -d public"
> java -jar target/scala-2.11/http-server-assembly.jar -p 5000 -d public"
```

### Logs
Logs are written to stdout and a file at `./logs`.

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

Route creation helper functions exist for the following HTTP methods: GET, POST, PUT, PATCH, DELETE, OPTIONS.  If you want to use another method you can use `Application::match` to specify your method.

```java
app.match("FOO", "/hello", request -> /* ... */);
```

## Application Middleware
Middleware allows you to run some code before or after each request.  It allows you to alter the behavior of the server without having to add dependencies to the core Flint library.  For example, each application can choose how it wants to do logging rather than depending on the way the server chooses to do it.  Other useful things it could be used for are caching and partial content responses.  It's also useful for reducing duplication and general organization.

```java
Application app = new Application()
    .before(loggerMiddleware::logRequest)
    .after(RangeMiddleware::handleRange);
```

Before middleware functions take a Request and returns a Request.  After middleware functions take a Request and a Response and return a Response.

## Route Middleware
Middleware can also be applied to Routes.

```java
app.get("/hello", request -> helloController::get)
    .before(authorizationMiddleware::authorize);
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
The PATCH method is intended to be used with a media type that describes the changes to be applied to the resource.  Such media types have been defined for JSON and XML, but none exist for text documents.  Since cob_spec does PATCH requests on text documents, I created a media type for the job and called it `application/unix-diff`.  It uses the unix diff format to describe changes and can be applied on the server using the unix patch command.

# Project Plan

## Current Sprint
**From** ??? **to** ???

| AV  | IP  | CP  | SP  | Description |
|:---:|:---:|:---:|:---:|-------------|

**Legend:** *AV* => Available, *IP* => In Progress, *CP* => Completed, *SP* => Story Points

## Backlog
| SP  | Description |
|:---:|-------------|
|  3  | Add support for URI Templates.  This would allow routes like `app.get("/foo/{id}", request -> ...)`.  Currently routes can only do exact matches which limits how dynamic the server can be.
|  2  | Multiple byte range partial content support.  Once single byte range support is complete, this shouldn't be too difficult.
|  2  | Decouple parser from flint and make it its own package.  If the dependency only goes one way, it could later be expanded into a general purpose tool for parsing anything HTTP related.  For example, an HTTP Client would require a parser that has a lot of overlap with the parsing needs for an HTTP Server.
|  2  | Introduce a way to group routes so middleware can be applied to an arbitrary group rather than just one Route or all Routes.
|  2  | Make default templates pluggable rather than hard-coded in Application

