/**
 * = Metrics
 *
 * This project implements the Vert.x Metrics Service Provider Interface (SPI) reporting metrics to the
 * https://github.com/dropwizard/metrics[Dropwizard metrics] library.
 *
 * == Features
 *
 * A fairly simple API to retrieve metrics via the {@link io.vertx.core.metrics.Measured Measured}
 * interface which is implemented by various Vert.x components like {@link io.vertx.core.http.HttpServer HttpServer},
 * {@link io.vertx.core.net.NetServer}, and even {@link io.vertx.core.Vertx Vertx} itself.
 *
 * Confiugrable JMX reporting based on Dropwizard implementation, exposing Vert.x as JMX MBeans.
 *
 * == Getting started
 *
 * To enable metrics first setup maven to include this as a dependency
 *
 * ----
 * <dependency>
 *   <groupId>io.vertx</groupId>
 *   <artifactId>vertx-metrics</artifactId>
 *   <version>${vertx.metrics.version}</version>
 * </dependency>
 * ----
 *
 * Then when you create vertx enable metrics using the {@link io.vertx.ext.dropwizard.DropwizardMetricsOptions}:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setup()}
 * ----
 *
 * You can also enable JMX:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupJMX()}
 * ----
 *
 * To see details about JMX see the <<jmx>> section at the bottom.
 *
 * == Metrics service
 *
 * == Naming
 *
 * Each measured component listed below (except for Vertx) will have a base name associated with it. Each metric
 * can be retrieved by providing the fully qualified name <fqn> `baseName` + `.` + `metricName` from Vertx:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#naming1}
 * ----
 *
 * or from the measured component itself using just the metric name:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#naming2}
 * ----
 *
 * See more examples below on how to retrieve/use metrics for a specific component.
 *
 * == Retrieving metrics
 *
 * Once enabled, the {@link io.vertx.ext.dropwizard.MetricsService} allows to retrieve metrics snapshots from any
 * {@link io.vertx.core.metrics.Measured Measured} object which provides a map of the metric name to the data,
 * represented by a {@link io.vertx.core.json.JsonObject}. So for example if we were to print out all metrics
 * for a particular Vert.x instance:
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#example1}
 * ----
 *
 * NOTE: For details on the actual contents of the data (the actual metric) represented by the {@link io.vertx.core.json.JsonObject}
 * consult the implementation documentation like https://github.com/vert-x3/vertx-metrics[vertx-metrics]
 *
 * Often it is desired that you only want to capture specific metrics for a particular component, like an http server
 * without having to know the details of the naming scheme of every metric (something which is left to the implementers of the SPI).
 *
 * Since {@link io.vertx.core.http.HttpServer HttpServer} implements {@link io.vertx.core.metrics.Measured}, you can easily grab all metrics
 * that are specific for that particular http server.
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#example3}
 * ----
 *
 * == Data
 *
 * Below is how each dropwizard metric is represented in JSON. Please refer to the
 * https://github.com/dropwizard/metrics[Dropwizard metrics] documentation for detailed information on each metric.
 *
 * [[gauge]]
 * === Gauge
 *
 * [source,javascript]
 * ----
 * {
 *   "value" : value // any json value
 * }
 * ----
 *
 * [[counter]]
 * === Counter
 *
 * [source,$lang]
 * ----
 * {
 *   "count" : 1 // number
 * }
 * ----
 *
 * [[histogram]]
 * === Histogram
 *
 * [source,javascript]
 * ----
 * {
 *   "count"  : 1 // long
 *   "min"    : 1 // long
 *   "max"    : 1 // long
 *   "mean"   : 1.0 // double
 *   "stddev" : 1.0 // double
 *   "median" : 1.0 // double
 *   "75%"    : 1.0 // double
 *   "95%"    : 1.0 // double
 *   "98%"    : 1.0 // double
 *   "99%"    : 1.0 // double
 *   "99.9%"  : 1.0 // double
 * }
 * ----
 *
 * [[meter]]
 * === Meter
 *
 * [source,$lang]
 * ----
 * {
 *   "count"             : 1 // long
 *   "meanRate"          : 1.0 // double
 *   "oneMinuteRate"     : 1.0 // double
 *   "fiveMinuteRate"    : 1.0 // double
 *   "fifteenMinuteRate" : 1.0 // double
 *   "rate"              : "events/second" // string representing rate
 * }
 * ----
 *
 * [[throughput]]
 * === Throughput
 *
 * [source,$lang]
 * ----
 * {
 *   "count"             : 40 // long
 *   "throughput"        : 3.4 // double - number of occurence per second
 *   "meanRate"          : 2.0 // double
 *   "oneMinuteRate"     : 1.0 // double
 *   "fiveMinuteRate"    : 1.0 // double
 *   "fifteenMinuteRate" : 1.0 // double
 *   "rate"              : "events/second" // string representing rate
 * }
 * ----
 *
 * [[timer]]
 * === Timer
 *
 * A timer is basically a combination of Histogram + Meter.
 *
 * [source,$lang]
 * ----
 * {
 *   // histogram data
 *   "count"  : 1 // long
 *   "min"    : 1 // long
 *   "max"    : 1 // long
 *   "mean"   : 1.0 // double
 *   "stddev" : 1.0 // double
 *   "median" : 1.0 // double
 *   "75%"    : 1.0 // double
 *   "95%"    : 1.0 // double
 *   "98%"    : 1.0 // double
 *   "99%"    : 1.0 // double
 *   "99.9%"  : 1.0 // double
 *
 *   // meter data
 *   "meanRate"          : 1.0 // double
 *   "oneMinuteRate"     : 1.0 // double
 *   "fiveMinuteRate"    : 1.0 // double
 *   "fifteenMinuteRate" : 1.0 // double
 *   "rate"              : "events/second" // string representing rate
 * }
 * ----
 *
 * == The metrics
 *
 * The following metrics are currently provided.
 *
 * === Vert.x metrics
 *
 * The following metrics are provided:
 *
 * * `vertx.event-loop-size` - A <<gauge>> of the number of threads in the event loop pool
 * * `vertx.worker-pool-size` - A <<gauge>> of the number of threads in the worker pool
 * * `vertx.cluster-host` - A <<gauge>> of the cluster-host setting
 * * `vertx.cluster-port` - A <<gauge>> of the cluster-port setting
 * * `vertx.verticles` - A <<counter>> of the number of verticles currently deployed
 *
 * === Event bus metrics
 *
 * Base name: `vertx.eventbus`
 *
 * * `handlers` - A <<counter>> of the number of event bus handlers
 * * `handlers.myaddress` - A <<timer>> representing the rate of which messages are being received for the _myaddress_ handler
 * * `messages.bytes-read` - A <<meter>> of the number of bytes read when receiving remote messages
 * * `messages.bytes-written` - A <<meter>> of the number of bytes written when sending remote messages
 * * `messages.pending` - A <<counter>> of the number of messages received but not yet processed by an handler
 * * `messages.pending-local` - A <<counter>> of the number of messages locally received but not yet processed by an handler
 * * `messages.pending-remote` - A <<counter>> of the number of messages remotely received but not yet processed by an handler
 * * `messages.received` - A <<throughput>> representing the rate of which messages are being received
 * * `messages.received-local` - A <<throughput>> representing the rate of which local messages are being received
 * * `messages.received-remote` - A <<throughput>> representing the rate of which remote messages are being received
 * * `messages.delivered` - A <<throughput>> representing the rate of which messages are being delivered to an handler
 * * `messages.delivered-local` - A <<throughput>> representing the rate of which local messages are being delivered to an handler
 * * `messages.delivered-remote` - A <<throughput>> representing the rate of which remote messages are being delivered to an handler
 * * `messages.sent` - A <<throughput>> representing the rate of which messages are being sent
 * * `messages.sent-local` - A <<throughput>> representing the rate of which messages are being sent locally
 * * `messages.sent-remote` - A <<throughput>> representing the rate of which messages are being sent remotely
 * * `messages.published` - A <<throughput>> representing the rate of which messages are being published
 * * `messages.published-local` - A <<throughput>> representing the rate of which messages are being published locally
 * * `messages.published-remote` - A <<throughput>> representing the rate of which messages are being published remotely
 * * `messages.reply-failures` - A <<meter>> representing the rate of reply failures
 *
 * The monitored event bus handlers is configurable via a match performed on the handler registration address.
 * Vert.x can have potentially a huge amount of registered event bus, therefore the only good default for this
 * setting is to monitor zero handlers.
 *
 * The monitored handlers can be configured in the {@link io.vertx.ext.dropwizard.DropwizardMetricsOptions} via
 * a specific address match or a regex match:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupMonitoredHandlers()}
 * ----
 *
 * WARNING: if you use regex match, a wrong regex can potentially match a lot of handlers.
 *
 * [[http-server-metrics]]
 * === Http server metrics
 *
 * Base name: `vertx.http.servers.<host>:<port>`
 *
 * Http server includes all the metrics of a <<net-server-metrics,Net Server>> plus the following:
 *
 * * `requests` - A <<timer>> of a request and the rate of it's occurrence
 * * `<http-method>-requests` - A <<timer>> of a specific http method request and the rate of it's occurrence
 * ** Examples: `get-requests`, `post-requests`
 * * `<http-method>-requests./<uri>` - A <<timer>> of a specific http method & URI request and the rate of it's occurrence
 * ** Examples: `get-requests./some/uri`, `post-requests./some/uri?foo=bar`
 * * `responses-1xx` - A <<meter>> of the 1xx response code
 * * `responses-2xx` - A <<meter>> of the 2xx response code
 * * `responses-3xx` - A <<meter>> of the 3xx response code
 * * `responses-4xx` - A <<meter>> of the 4xx response code
 * * `responses-5xx` - A <<meter>> of the 5xx response code
 *
 * Http URI metrics must be explicitely configured in the options either by exact match or regex match:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupMonitoredUris()}
 * ----
 *
 * *For `bytes-read` and `bytes-written` the bytes represent the body of the request/response, so headers, etc are ignored.*
 *
 * === Http client metrics
 *
 * Base name: `vertx.http.clients.@<id>`
 *
 * Http client includes all the metrics of a <<http-server-metrics,Http Server>> plus the following:
 *
 * * `connections.max-pool-size` - A <<gauge>> of the max connection pool size
 * * `connections.pool-ratio` - A ratio <<gauge>> of the open connections / max connection pool size
 * * `responses-1xx` - A <<meter>> of the 1xx response code
 * * `responses-2xx` - A <<meter>> of the 2xx response code
 * * `responses-3xx` - A <<meter>> of the 3xx response code
 * * `responses-4xx` - A <<meter>> of the 4xx response code
 * * `responses-5xx` - A <<meter>> of the 5xx response code
 *
 * [[net-server-metrics]]
 * === Net server metrics
 *
 * Base name: `vertx.net.servers.<host>:<port>`
 *
 * * `open-connections` - A <<counter>> of the number of open connections
 * * `open-connections.<remote-host>` - A <<counter>> of the number of open connections for a particular remote host
 * * `connections` - A <<timer>> of a connection and the rate of it's occurrence
 * * `exceptions` - A <<counter>> of the number of exceptions
 * * `bytes-read` - A <<histogram>> of the number of bytes read.
 * * `bytes-written` - A <<histogram>> of the number of bytes written.
 *
 * === Net client metrics
 *
 * Base name: `vertx.net.clients.@<id>`
 *
 * Net client includes all the metrics of a <<net-server-metrics,Net Server>>
 *
 * === Datagram socket metrics
 *
 * Base name: `vertx.datagram`
 *
 * * `sockets` - A <<counter>> of the number of datagram sockets
 * * `exceptions` - A <<counter>> of the number of exceptions
 * * `bytes-written` - A <<histogram>> of the number of bytes written.
 * * `<host>:<port>.bytes-read` - A <<histogram>> of the number of bytes read.
 * ** This metric will only be available if the datagram socket is listening
 *
 * [[jmx]]
 * == JMX
 *
 * JMX is disabled by default.
 *
 * If you want JMX, then you need to enabled that:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupJMX()}
 * ----
 *
 * If running Vert.x from the command line you can enable metrics and JMX by uncommented the JMX_OPTS line in the
 * `vertx` or `vertx.bat` script:
 *
 * ----
 * JMX_OPTS="-Dcom.sun.management.jmxremote -Dvertx.options.jmxEnabled=true"
 * ----
 *
 * You can configure the domain under which the MBeans will be created:
 *
 * [source,$lang]
 * ----
 * {@link examples.MetricsExamples#setupJMXWithDomain()}
 * ----
 *
 * == Enabling remote JMX
 *
 * If you want the metrics to be exposed remotely over JMX, then you need to set, at minimum the following system property:
 *
 * `com.sun.management.jmxremote`
 *
 * If running from the command line this can be done by editing the `vertx` or `vertx.bat` and uncommenting the
 * `JMX_OPTS` line.
 *
 * Please see the http://docs.oracle.com/javase/8/docs/technotes/guides/management/agent.html[Oracle JMX documentation] for more information on configuring JMX
 *
 * *If running Vert.x on a public server please be careful about exposing remote JMX access*
 *
 * == Accessing Dropwizard Registry
 *
 * When configuring the metrics service, an optional registry name can be specified for registering the underlying
 * https://dropwizard.github.io/metrics/3.1.0/getting-started/#the-registry[Dropwizard Registry] in the
 * the https://dropwizard.github.io/metrics/3.1.0/apidocs/com/codahale/metrics/SharedMetricRegistries.html[Dropwizard Shared Registry]
 * so you can retrieve this registry and use according to your needs.
 *
 * [source,java]
 * ----
 * VertxOptions options = new VertxOptions().setMetricsOptions(
 *   new MetricsServiceOptions().setEnabled(true).setRegistryName("the_name")
 * );
 * Vertx vertx = Vertx.vertxt(options);
 *
 * // Get the registry
 * MetricRegistry registry = SharedMetricRegistries.getOrCreate("the_name");
 *
 * // Do whatever you need with the registry
 * ----
 */
@GenModule(name = "vertx-dropwizard")
@Document(fileName = "index.adoc")
package io.vertx.ext.dropwizard;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;