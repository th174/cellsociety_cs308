#!/usr/bin/perl
use strict;
use warnings;


#change this between simulations:
my $simulationType = "Segregation";
my @state = ("EMPTY","X","O");              #for Segregation
#my @state = ("DEAD", "ALIVE");              #for GameOfLife
#my @state = ("EMPTY", "PREDATOR", "PREY");   #for PredatorPrey
#my @state = ("EMPTY", "TREE", "BURNING");   #for Fire

my $additionalArgs = "\n\t\t<Threshold>.7</Threshold>";    #for Segregation
#my $additionalArgs = "\n\t\t<ProbCatch>.55</ProbCatch>";   #for Fire
#my $additionalArgs = "";                                    #for the rest

my $xSize = 50;
my $ySize = 50;
my $fps = 3;
#for random state:
my $randomState = "rand";

open(OUTPUT, "> $simulationType.xml");
print OUTPUT '<?xml version="1.0" encoding="UTF-8" ?>';
print OUTPUT "\n<Simulation type=\"$simulationType\" width=\"$xSize\" height=\"$ySize\" fps=\"$fps\">\n";

#################################################################
#You can initialize entire rows and colums by leaving the attribute blank
#For example, xPos="3" initializes the entire 3rd column
#xPos="3" yPos="5" initializes the single cell 3,5
#Use $randomState for random state

#usage: initSomething(state,arrayref)
#################################################################
#CHANGE THIS FOR INITIAL STATE

initGrid($randomState);
#initRows($state[2], [ $ySize / 2 ]);
#initColumn($state[1], [ 0, 2, 4, 6, 8, 10, 14, 16 ]);
#initCells($state[1], [ [ 1, 2 ], [ 2, 3 ], [ 3, 3 ], [ 3, 2 ], [ 3, 1 ] ]);
#for (my $x = 1; $x <= $xSize; $x++) {
#    for (my $y = 1; $y <= $ySize; $y++) {
#        if ($x >= 4 && $x < 7 && $y >= 5 && $y < 13 && !($x == 5 && ($y == 6 || $y == 11 ))) {
#            initCells($state[1], [ [ $x, $y ] ]);
#        }
#    }
#}

#################################################################


sub initCells {
    my $state = shift;
    my $cells = shift;
    foreach my $cell (@$cells) {
        my $x = @$cell[0];
        my $y = @$cell[1];
        print OUTPUT
            "\t<Cell xPos=\"$x\" yPos=\"$y\">".
                "\n\t\t<State>$state</State>".
                $additionalArgs.
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
                $additionalArgs.
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
                $additionalArgs.
                "\n\t</Cell>\n";
    }
}

sub initGrid {
    my $state = shift;
    print OUTPUT
        "\t<Cell>".
            "\n\t\t<State>$state</State>".
            $additionalArgs.
            "\n\t</Cell>\n";
}

print OUTPUT "</Simulation>\n";
close OUTPUT;
