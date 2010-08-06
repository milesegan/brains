#!/usr/bin/env ruby

schools = <<SCHOOLS
Cynicism
Epicureanism
Existentialism
Marxism
Transcendentalism
Objectivism
Platonism
Pragmatism
Skepticism
Thomism
Dialectisism
Atomism
Epicureanism
Hedonism
Utilitarianism
SCHOOLS

people = <<PEOPLE
Adorno
Aquinas
Aristotle
Augustine
Bacon
Barthes
Bataille
Baudrillard
Benjamin
Berkeley
PEOPLE

people = people.split("\n")
schools = schools.split("\n")
srand(Time.now.to_i)

people.each do |person|
  schools = schools.sort_by { rand }
  (0..4).each do |i|
    puts [person, schools[i], rand(3) + 1].join(":")
  end
end
