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
#You can initialize entire rows and colums by leaving the attribute blank
#For example, xPos="3" initializes the entire 3rd column
#xPos="3" yPos="5" initializes the single cell 3,5

#usage: initSomething(state,arrayref)
initGrid($state[0]);
#initRows($state[1], [ 5, 3, 4 ]);
#initColumn($state[1], [ 4, 7, 9 ]);
#initCells($state[1], [ [ 1, 2 ], [ 2, 5 ], [ 7, 9 ] ]);
for (my $x = 1; $x <= $xSize; $x++) {
    for (my $y = 1; $y <= $ySize; $y++) {
        if ($x >= 4 && $x < 7 && $y >= 5 && $y < 13 && !($x == 5 && ($y == 6 || $y == 11 ))) {
            initCells($state[1], [ [ $x, $y ] ]);
        }
    }
}

sub initCells {
    my $state = shift;
    my $cells = shift;
    foreach my $cell (@$cells) {
        my $x = @$cell[0];
        my $y = @$cell[1];
        print OUTPUT
            "\t<Cell xPos=\"$x\" yPos=\"$y\">".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initRows {
    my $state = shift;
    my $rows = shift;
    foreach my $y (@$rows) {
        print OUTPUT
            "\t<Cell yPos=\"$y\">".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initColumn {
    my $state = shift;
    my $columns = shift;
    foreach my $x (@$columns) {
        print OUTPUT
            "\t<Cell xPos=\"$x\">".
                "\n\t\t<State>$state</State>".
                "\n\t</Cell>\n";
    }
}

sub initGrid {
    my $state = shift;
    print OUTPUT
        "\t<Cell>".
            "\n\t\t<State>$state</State>".
            "\n\t</Cell>\n";
}

print OUTPUT "</Simulation>\n";
close OUTPUT;
