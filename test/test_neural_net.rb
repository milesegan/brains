require 'brains'
require 'test/unit'

class NeuralNetTest < Test::Unit::TestCase

  def test_it_should_instantiate
    n = Brains::NeuralNet.new(3, 5, 1)
    assert_equal(n.inputs, 3)
    assert_equal(n.hweights.column_size, 5)
    assert_equal(n.oweights.row_size, 6)
  end

end
