package com.saramambiches.datingapp;

import com.yuyakaido.android.cardstackview.Direction;

import java.util.Arrays;
import java.util.List;

public enum DirectionCustom {
    Left,
    Right,
    Top,
    Bottom;

    public static final List<Direction> HORIZONTAL = Arrays.asList(Direction.Left, Direction.Right);
    public static final List<Direction> VERTICAL = Arrays.asList(Direction.Top, Direction.Bottom);
    public static final List<Direction> CUSTOM = Arrays.asList(Direction.Top, Direction.Left, Direction.Right);
    public static final List<Direction> FREEDOM = Arrays.asList(Direction.values());
}
