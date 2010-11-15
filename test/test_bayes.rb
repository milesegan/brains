require 'test/unit'
require 'lib/bayes'
require 'lib/datafile'

class BayesTest < Test::Unit::TestCase

  def test_it_trains
    d = DataFile.new("data/mushroom.csv")
    b = Bayes.new
    b.train(d)
    assert_equal b.count, 8124
  end

  def test_it_classifies
    d = DataFile.new("data/mushroom.csv")
    b = Bayes.new
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
