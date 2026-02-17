#!/usr/bin/env bash

export PGDATA="$HOME/postgres_data"
export PGHOST="/tmp"
export PGPORT="5432"
initdb --locale "$LANG" -E UTF8
postgres -k "$PGHOST" &
