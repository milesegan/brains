# Naive bayes classifier.

class Bayes
  attr_reader :count, :pc, :pf
  def initialize()
    @count = 0.0
    @pc = {}
    @pf = {}
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

  def classify(features)
    probs = {}
    @pc.keys.each do |klass|
      p = 1.0
      features.each do |f,v|
        p *= @pf.fetch(f, {}).fetch(v, {}).fetch(klass, 0.01) / @count
      end
      probs[klass] = p
    end
    probs.to_a.sort { |a,b| b.last <=> a.last }.first.first
  end

end
