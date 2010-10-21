require 'test/unit'

class TestDataFile < Test::Unit::TestCase
  
  def test_it_loads
    d = DataFile.new("data/mushroom.csv")
    assert_equal(d.names.first, "cap-shape")
    c = []
    d.each_sample { |s| c << s }
    assert_equal c.size, d.size
  end

  def test_it_shuffles
    d = DataFile.new("data/mushroom.csv")
    d.read!
    f = d.samples[0,10]
    d.shuffle!
    assert_not_equal f, d.samples[0,10]
  end

  def test_it_splits
    d = DataFile.new("data/mushroom.csv")
    a, b = d.split(4)
    assert_equal a.size, d.size / 4
    assert_equal b.size, d.size - a.size
  end

end
