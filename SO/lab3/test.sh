#!/bin/bash

root_directory="~/University/second_Year/"
destination_directory="~/University/personal_GitHub/secondYear/"

copy_command_withoutPP="rsync -av --exclude='PP' $root_directory $destination_directory"

root_dir="~/University/second_Year/PP/pp-1208a-homeworks-Gabi-TUIASI/*"
destination_dir="~/University/personal_GitHub/secondYear/PP/"

copy_command_withPP="cp -r $root_dir $destination_dir"

eval "$copy_command_withoutPP"
eval "$copy_command_withPP"
