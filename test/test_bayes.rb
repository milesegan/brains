require 'test/unit'
require 'lib/bayes'
require 'lib/datafile'

class BayesTest < Test::Unit::TestCase

  def test_it_trains
    bc = Bayes.new
    bc.train("class", [[:f, "a"], [:f, "b"], [:f, "c"]])
    assert bc.count == 1
  end

  def test_it_loads_from_datafile
    d = DataFile.load("data/mushroom.csv")
    b = Bayes.load_from_datafile(d)
    assert_equal b.count, 8124
  end

  def test_it_loads_from_json
    d = open("test/bc.json")
    b = Bayes.load_from_json(d)
    assert_equal b.count, 6124
  end

  def test_it_classifies
    d = DataFile.load("data/mushroom.csv")
    d.shuffle!
    b = Bayes.new
    test, train = d.split(4)
    train.each do |k,f| 
      b.train(k, d.names.zip(f))
    end
    correct = 0
    10.times do |i|
      p = d.names.zip(test[i][1])
      if b.classify(p) == test[i][0]
        correct += 1
      end
    end
    assert correct.to_f / 10 >= 0.9, "bad classify"
  end

end
