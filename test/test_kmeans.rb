$: << File.join(File.dirname(__FILE__), "..", "lib")

require 'test/unit'
require 'brains'

class KMeansTest < Test::Unit::TestCase

  def test_it_should_cluster
    d = Brains::DataFile.make_real
    k = Brains::KMeans.new
    clusters = k.cluster(4, d)
    assert_equal clusters.size, 4
  end
  
end
