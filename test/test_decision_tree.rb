$: << File.join(File.dirname(__FILE__), "..", "lib")

require 'test/unit'
require 'brains'

class DecisionTreeTest < Test::Unit::TestCase

  def test_it_classifies
    d = Brains::DataFile.new("data/mushroom.csv")
    test, train = d.split(4, true)
    t = Brains::DecisionTree.new(train)
    correct = 0
    test.each do |klass,features|
      c = t.classify(features)
      correct += 1 if klass == c
    end
    assert correct.to_f / test.size >= 0.9, "bad classify"
  end

end
