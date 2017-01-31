#!/usr/bin/perl
use strict;
use warnings;

my $xSize = 16;
my $ySize = 16;
my $fps = 2;
my @state = ("DEAD", "ALIVE" );
my $simulationType = "GameOfLife";

open(OUTPUT, "> $simulationType.xml");

print OUTPUT '<?xml version="1.0" encoding="UTF-8" ?>';
print OUTPUT "\n<Simulation type=\"$simulationType\" width=\"@{[$xSize+2]}\" height=\"@{[$ySize+2]}\" fps=\"$fps\">\n";
#You can initialize entire rows and colums with "*"

initGrid($state[0]);
initRows($state[1], [1,2,3]);
#initColumn($state[1]);
#initCells($state[1],[[1,2]]);

sub initCells {
    my $state = shift;
    foreach (shift) {
        my $x = shift;
        my $y = shift;
        print OUTPUT
            "\t<Cell xPos=\"$x\" yPos=\"$y\">".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initRows {
    my $state = shift;
    foreach my $y (shift) {
        print OUTPUT
            "\t<Cell yPos=\"$y\>".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initColumn {
    my $state = $_[0];
    foreach my $x (shift) {
        print OUTPUT
            "\t<Cell xPos=\"$x\">".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initGrid{
    my $state = shift;
    print OUTPUT
        "\t<Cell>".
            "\n\t\t<State>$state</State>".
            "\n\t</Cell>\n";
}

print OUTPUT "</Simulation>\n";
close OUTPUT;
