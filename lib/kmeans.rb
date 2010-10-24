# Classifies by K-Means method
# http://en.wikipedia.org/wiki/K-means_clustering
class KMeans

  # Clusters points into k clusters by euclidean distance.
  def cluster(k, points)
    centroids = pick_initial_centroids(k, points)
    while true
      newp = points.group_by { |p| closest_centroid(centroids, p) }
      newc = k.times.collect { |i| centroid(newp[i] || []) }
      return newp if newc == centroids
      centroids = newc
    end
  end

  private
  def closest_centroid(centroids, p)
    distances = centroids.collect { |c| distance(p, c) }
    closest = centroids.size.times.sort do |a,b| 
      distances[a] - distances[b]
    end
    closest.first
  end

  private
  def pick_initial_centroids(k, points)
    points.shuffle[0, k]
  end

  private
  def distance(a, b)
    dist = a.zip(b).collect { |i| (i.first - i.last) ** 2 }
    d = dist.inject(0) { |a,b| a + b }
  end

  private
  def centroid(points)
    return nil if points.empty?
    size = points.first.size
    sums = Array.new(size, 0.0)
    size.times do |i|
      points.each do |p|
        sums[i] += p[i]
      end
      sums[i] = sums[i] / points.size
    end
    sums
  end
end
