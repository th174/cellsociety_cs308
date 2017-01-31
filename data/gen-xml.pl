#!/usr/bin/perl
use strict;
use warnings;

open(OUTPUT, "> gameOfLife.xml");

my $xSize = 16;
my $ySize = 16;
my $fps = 2;
my $state1 = "ALIVE";
my $state2 = "EMPTY";
my $simulationType = "GameOfLife";

my $defaultState = "DEAD";

print OUTPUT '<?xml version="1.0" encoding="UTF-8" ?>';
print OUTPUT "\n<Simulation type=\"$simulationType\" width=\"@{[$xSize+2]}\" height=\"@{[$ySize+2]}\" fps=\"$fps\">\n";
print OUTPUT
    "\t<DefaultCell>".
        "\n\t\t<State>$defaultState</State>".
        "\n\t</DefaultCell>\n";
for (my $i = 1; $i <= $xSize; $i++) {
    for (my $j = 1; $j <= $ySize; $j++) {
        if ($i >= 4 && $i < 7 && $j >= 5 && $j < 13 && !($i == 5 && ($j == 6 || $j == 11 ))) {
            print OUTPUT
                "\t<Cell xPos=\"$i\" yPos=\"$j\">".
                    "\n\t\t<State>$state1</State>".
                    "\n\t</Cell>\n";
        }
        if ($i >= 4 && $i < 7 && $j >= 5 && $j < 13 && !($i == 5 && ($j == 6 || $j == 11 ))) {
            print OUTPUT
                "\t<Cell xPos=\"$i\" yPos=\"$j\">".
                    "\n\t\t<State>$state1</State>".
                    "\n\t</Cell>\n";
        }
    }
}
print OUTPUT "</Simulation>\n";
close OUTPUT;
