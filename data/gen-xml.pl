#!/usr/bin/perl
use strict;

open(OUTPUT,"> gameOfLife.xml");

my $xSize = 4;
my $ySize = 4;
my $state = "ALIVE";
my $simulationType = "GameOfLife";

my $defaultState = "DEAD";

print OUTPUT '<?xml version="1.0" encoding="UTF-8" ?>';
print OUTPUT "\n<Simulation width=\"@{[$xSize+2]}\" height=\"@{[$ySize+2]}\" type=\"$simulationType\" defaultState=\"$defaultState\">\n";
for(my $i = 0; $i < $xSize; $i++){
    for (my $j = 0; $j < $ySize; $j++){
        if (($i < 2 && $j < 2) || ($i >= 2 && $j >= 2)){
            print OUTPUT 
            "\t<Cell type=\"$simulationType\">".
            "\n\t\t<State>$state</State>".
            "\n\t\t<xPos>@{[$i+1]}</xPos>".
            "\n\t\t<yPos>@{[$j+1]}</yPos>".
            "\n\t</Cell>\n";
        }
    }
}
print OUTPUT "</Simulation>\n";
close OUTPUT;
