package main

import "testing"

func TestSum5and5(t *testing.T) {
	total := Sum(5, 5)
	if total != 10 {
		t.Errorf("Sum incorrect, got: %d, want: %d.", total, 10)
	}
}

func TestSum3and5(t *testing.T) {
	total := Sum(3, 5)
	if total != 8 {
		t.Errorf("Sum incorrect, got: %d, want: %d.", total, 8)
	}
}
