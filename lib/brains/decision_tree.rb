module Brains

# A decision-tree classifier.
class DecisionTree

  def initialize(points)
    @most_common_class = nil
    @tree = train(points)
  end

  # Returns the class predicted for the given features.
  def classify(features)
    tree = @tree
    while true
      v = features[tree[0]]
      return @most_common_class unless v
      n = tree[1][v]
      if n.nil?
        return @most_common_class
      elsif n.class == String
        return n
      else
        tree = n
      end
    end
  end

  private
  def train(points)
    return nil if points.empty?

    pm = ProbabilityMap.new(points)
    if @most_common_class.nil?
      # save for later as default value
      @most_common_class = pm.most_common_class
    end
    return pm.classes.first if pm.classes.size == 1
    return nil if pm.features.empty?

    # find highest entropy feature to split with
    igains = pm.features.collect { |f| [info_gain(f, pm), f] }.sort.reverse
    best = igains.shift.last
    rest = igains.collect(&:last)
    tree = [best, {}]
    
    # split remaining data points on best feature
    pm.pf[best].keys.each do |val|
      newp = []
      points.each do |p|
        if p[1][best] == val
          p[1].delete(best)
          newp << p
        end
      end
      tree[1][val] = train(newp)
    end
    tree
  end

  def info_gain(feature, pm)
    class_e = pm.pc.keys.collect { |c| entropy(pm.pc[c] / pm.count) }
    all_e = []
    pm.pc.keys.each do |c|
      pm.pf.fetch(feature, {}).each do |v,pv|
        pv = sum(pv.values)
        p = pm.getpfc(feature, v, c)
        if p
          all_e << entropy(p / pm.count) * pv / pm.count
        end
      end
    end
    sum(class_e) - sum(all_e)
  end

  def sum(vals)
    vals.inject(0) { |a,b| a + b }
  end

  def entropy(p)
    -p * Math.log(p)
  end
end

end
