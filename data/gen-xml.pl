#!/usr/bin/perl
use strict;
use warnings;

open(OUTPUT,"> PredatorPrey.xml");

my $xSize = 16;
my $ySize = 16;
my $fps = 2;
my $state1 = "PREDATOR";
my $state2 = "PREY";
my $simulationType = "PredatorPrey";

my $defaultState = "EMPTY";

print OUTPUT '<?xml version="1.0" encoding="UTF-8" ?>';
print OUTPUT "\n<Simulation type=\"$simulationType\" width=\"@{[$xSize+2]}\" height=\"@{[$ySize+2]}\" fps=\"$fps\" defaultState=\"$defaultState\">\n";
for(my $i = 1; $i <= $xSize; $i++){
    for (my $j = 1; $j <= $ySize; $j++){
        #if ($i >= 4 && $i < 7 && $j >= 5 && $j < 13 && !($i == 5 && ($j == 6 || $j == 11 ))){
        if ($i ==1 && $j==1 ){
            print OUTPUT 
            "\t<Cell>".
            "\n\t\t<State>$state1</State>".
            "\n\t\t<xPos>$i</xPos>".
            "\n\t\t<yPos>$j</yPos>".
            "\n\t</Cell>\n";
        }
        #if ($i >= 4 && $i < 7 && $j >= 5 && $j < 13 && !($i == 5 && ($j == 6 || $j == 11 ))){
            if ($i==3 && $j==3){
            print OUTPUT
            "\t<Cell type=\"$simulationType\">".
            "\n\t\t<State>$state2</State>".
            "\n\t\t<xPos>$i</xPos>".
            "\n\t\t<yPos>$j</yPos>".
            "\n\t</Cell>\n";
        }
    }
}
print OUTPUT "</Simulation>\n";
close OUTPUT;
