require 'test/unit'

class TestDataFile < Test::Unit::TestCase
  
  def test_it_loads
    d = DataFile.new("data/mushroom.csv")
    assert_equal(d.names.first, :"cap-shape")
    assert_equal d.to_a.size, 8124
  end

  def test_it_splits
    d = DataFile.new("data/mushroom.csv")
    a, b = d.split(4)
    assert_equal a.size, d.size / 4
    assert_equal b.size, d.size - a.size
  end

end
