module Brains

# Naive bayes classifier.
class Bayes

  attr_reader :count, :pc, :pf

  def initialize
    @pmap = ProbabilityMap.new
  end

  def train(points)
    @pmap.train(points)
  end

  def classify(features)
    probs = {}
    @pmap.pc.keys.each do |klass|
      p = 1.0
      features.each do |f,v|
        p *= @pmap.pf.fetch(f, {}).fetch(v, {}).fetch(klass, 0.01) / @pmap.count
      end
      probs[klass] = p
    end
    probs.to_a.sort { |a,b| b.last <=> a.last }.first.first
  end

end

end
