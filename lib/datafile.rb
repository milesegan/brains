# datafile loading library

class DataFile

  attr_reader :size, :names, :samples

  def initialize(path)
    @file = open(path)
    @sep = /\s*,\s*/
    while line = @file.readline
      break unless line =~ /^\s*#/ # skip comments
    end
    klass, *@names = line.strip.split(@sep)
    @names.collect! { |i| i.to_sym }
    @size = 0
    @samples = nil
  end

  def each_sample
    @file.each_line do |f|
      @size += 1
      klass, *parts = f.strip.split(@sep)
      next unless parts.size == @names.size
      yield [klass, parts]
    end
  end

  def read!
    if @samples.nil?
      @samples = []
      each_sample { |k,f| @samples << [k, f] }
    end
  end

  def shuffle!
    read!
    @samples = @samples.shuffle
  end

  def split(fraction)
    read!
    index = @samples.size / fraction
    [@samples[0, index], @samples[index, @samples.size - 1]]
  end

end
