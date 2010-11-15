# datafile loading class
class DataFile

  include Enumerable

  attr_reader :size, :names, :samples
  SEP = /\s*,\s*/

  def initialize(path)
    @file = open(path)
    while line = @file.readline
      break unless line =~ /^\s*#/ # skip comments
    end
    klass, *@names = line.strip.split(SEP)
    @names.collect! { |i| i.to_sym }
    @size = 0
  end

  def each
    @file.each_line do |f|
      @size += 1
      klass, *parts = f.strip.split(SEP)
      next unless parts.size == @names.size
      parts = Hash[@names.zip(parts)]
      yield [klass, parts]
    end
  end

  def split(fraction, shuffle = false)
    all = to_a
    all = all.shuffle if shuffle
    index = all.size / fraction
    [all[0..index-1], all[index..-1]]
  end

end
