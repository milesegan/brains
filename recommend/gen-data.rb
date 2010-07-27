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
SCHOOLS

people = <<PEOPLE
Abelard
Adorno
Aquinas
Arendt
Aristotle
Augustine
Bacon
Barthes
Bataille
Baudrillard
Beauvoir
Benjamin
Berkeley
PEOPLE

people = people.split("\n")
schools = schools.split("\n")
srand(Time.now.to_i)

people.each do |person|
  schools = schools.sort_by { rand }
  (0..5).each do |i|
    puts [person, schools[i], rand(3) + 1].join(":")
  end
end
