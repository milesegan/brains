$: << File.join(File.dirname(__FILE__), "..", "lib")

require 'test/unit'
require 'brains'

class BayesTest < Test::Unit::TestCase

  def test_it_trains
    d = Brains::DataFile.new("data/mushroom.csv")
    b = Brains::Bayes.new
    b.train(d)
    assert_equal b.count, 8124
  end

  def test_it_classifies
    d = Brains::DataFile.new("data/mushroom.csv")
    b = Brains::Bayes.new
    test, train = d.split(4, true)
    b.train(train)
    correct = 0
    test.each do |klass,features|
      c = b.classify(features)
      correct += 1 if klass == c
    end
    assert correct.to_f / test.size >= 0.9, "bad classify"
  end

end
