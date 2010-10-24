require 'test/unit'
require 'lib/kmeans'
require 'lib/datafile'

class KMeansTest < Test::Unit::TestCase

  def test_it_clusters
    d = DataFile.new("data/iris.csv")
    points = []
    d.each_sample do |s| 
      points << s.last.collect(&:to_f)
    end
    k = KMeans.new
    clusters = k.cluster(4, points)
    assert_equal clusters.size, 4
  end
  
end
