#!/usr/bin/env ruby

class MovieLens

  attr_reader :movie_ratings, :user_ratings, :movies

  def initialize(users, movies, ratings)
    @user_ratings = {}
    @movie_ratings = {}
    @movies = {}

    open(users).each do |u|
      id, *rest = u.split("::")
      @user_ratings[id.to_i] = {}
    end
    open(movies).each do |m|
      id, title, *rest = m.split("::")
      @movie_ratings[id.to_i] = {}
      @movies[id.to_i] = title
    end
    open(ratings).each do |m|
      userid, movieid, rating = m.split("::").collect { |i| i.to_i }
      @user_ratings[userid][movieid] = rating
      @movie_ratings[movieid][userid] = rating
    end
  end

  def recommend(user)
  end

  # calculate similarity of two movies via pearson correlation
  def similarity(a, b)
    a_ratings = @movie_ratings[a] || []
    b_ratings = @movie_ratings[b] || []
    return 0.0 if a_ratings.empty? or b_ratings.empty?
    mean_all_a = mean(a_ratings.values)
    mean_all_b = mean(b_ratings.values)
    common_a = []
    common_b = []

    # find ratings of other movies by same user
    # subtract mean and store deltas
    @movie_ratings[a].each do |user,rating_a|
      rating_b = @movie_ratings[b][user]
      if rating_b
        common_a << rating_a - mean_all_a
        common_b << rating_b - mean_all_b
      end
    end
    return 0.0 if common_a.size < 3
    pearson_correlation(common_a, common_b)
  end

  def estimate_rating(user, movie)
    rating = movie_ratings[user][movie]
    return rating if rating

    weighted_sum = 0.0
    num = 0
    user_ratings[user].each do |m,rating|
      sim = similarity(movie, m)
      weight = sim * rating
      weighted_sum += weight
      num += 1
    end
    
    if num > 0
      weighted_sum / num
    else
      0.0
    end
  end

  def pearson_correlation(a, b)
    return 0.0 if a.size == 0 # no overlapping ratings

    xy = 0.0
    mean_a, dev_a = mean_and_standard_deviation(a)
    mean_b, dev_b = mean_and_standard_deviation(b)

    a.size.times do |i|
      xy += (a[i] - mean_a) * (b[i] - mean_b)
    end

    if dev_a.zero? || dev_b.zero?
      ind_a = ind_b = 0.0
      (1 .. a.size - 1).each do |i|
        ind_a += a[i - 1] - a[i]
        ind_b += b[i - 1] - b[i]
      end

      if ind_a.zero? && ind_b.zero?
        # degenerate correlation, all points the same
        return 1.0
      elsif dev_a.zero?
        # otherwise either a or b vary
        dev_a = dev_b
      else
        dev_b = dev_a
      end
    end
      
    xy / (a.size * dev_a * dev_b)
  end

  def mean(values)
    values.inject {|sum,x| sum + x } / values.size
  end

  def mean_and_standard_deviation(values)
    m = mean(values)
    sigma = 0.0
    values.each do |v|
      sigma += (v - m) ** 2
    end
    sigma = sigma / values.size
    dev = Math.sqrt(sigma)
    [m, dev]
  end

end

$stderr.puts "loading"
set = MovieLens.new(*ARGV)
user = 1
ratings = []
$stderr.puts "calculating sims"
set.movie_ratings.keys[0..10].each do |m|
  rating = set.estimate_rating(user, m)
  ratings << [rating, set.movies[m]] unless rating.zero?
end
ratings.sort!
puts "estimated ratings for user #{user}"
ratings.each { |i| printf("%.04f %s\n", *i) }

