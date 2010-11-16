$: << File.join(File.dirname(__FILE__), "..", "lib")

require 'test/unit'
require 'brains'

class TestDataFile < Test::Unit::TestCase
  
  def test_it_loads
    d = Brains::DataFile.new("data/mushroom.csv")
    assert_equal(d.names.first, :"cap-shape")
    assert_equal d.to_a.size, 8124
  end

  def test_it_splits
    d = Brains::DataFile.new("data/mushroom.csv")
    a, b = d.split(4)
    assert_equal a.size, d.size / 4
    assert_equal b.size, d.size - a.size
  end

end
