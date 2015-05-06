require 'vertx/measured'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.dropwizard.MetricsService
module VertxDropwizard
  #  @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
  class MetricsService
    # @private
    # @param j_del [::VertxDropwizard::MetricsService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxDropwizard::MetricsService] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx
    # @return [::VertxDropwizard::MetricsService]
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::VertxDropwizard::MetricsService.new(Java::IoVertxExtDropwizard::MetricsService.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del))
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx)"
    end
    #  Will return the metrics that correspond with this measured object, null if no metrics is available.
    # @param [::Vertx::Measured] o
    # @return [Hash{String => Object}] the map of metrics where the key is the name of the metric (excluding the base name) and the value is the json data representing that metric
    def get_metrics_snapshot(o=nil)
      if o.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:getMetricsSnapshot, [Java::IoVertxCoreMetrics::Measured.java_class]).call(o.j_del) != nil ? JSON.parse(@j_del.java_method(:getMetricsSnapshot, [Java::IoVertxCoreMetrics::Measured.java_class]).call(o.j_del).encode) : nil
      end
      raise ArgumentError, "Invalid arguments when calling get_metrics_snapshot(o)"
    end
    # @param [::Vertx::Measured] measured
    # @return [String]
    def get_base_name(measured=nil)
      if measured.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:getBaseName, [Java::IoVertxCoreMetrics::Measured.java_class]).call(measured.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling get_base_name(measured)"
    end
  end
end
