module Brains

# Feature -> class probability mapper.
class ProbabilityMap

  attr_reader :count, :pc, :pf

  def initialize(points = nil)
    @count = 0.0
    @pc = {}
    @pf = {}
    train(points) if points
  end

  def classes
    @pc.keys
  end

  def features
    @pf.keys
  end

  def most_common_class
    @pc.to_a.sort { |a,b| a.last <=> b.last }.last.first
  end

  def getpfc(feature, value, klass)
    @pf.fetch(feature, {}).fetch(value, {})[klass]
  end

  def train(points)
    points.each do |klass,features|
      @count += 1
      @pc[klass] ||= 0
      @pc[klass] += 1
      features.each do |f,v|
        @pf[f] ||= {}
        @pf[f][v] ||= {}
        @pf[f][v][klass] ||= 0
        @pf[f][v][klass] += 1
      end
    end
  end

end

end
