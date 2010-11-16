module Brains

# Classifies by K-Means method
# http://en.wikipedia.org/wiki/K-means_clustering
class KMeans

  # Clusters points into k clusters by euclidean distance.
  def cluster(k, points)
    centroids = pick_initial_centroids(k, points)
    while true
      newp = points.group_by { |p| closest_centroid(centroids, p) }
      newc = k.times.collect { |i| centroid(newp[i]) or centroids[i] }
      return newp.values if newc == centroids
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
    points.shuffle[0..k-1]
  end

  private
  def distance(a, b)
    dist = 0.0
    a.keys.each do |k|
      dist += (a[k] - b[k]) ** 2
    end
    dist
  end

  private
  def centroid(points)
    return nil if points.nil?
    c = Hash.new(0.0)
    points.first.keys.each do |k|
      points.each { |p| c[k] += p[k] }
      c[k] /= points.size
    end
    c
  end

end

end
