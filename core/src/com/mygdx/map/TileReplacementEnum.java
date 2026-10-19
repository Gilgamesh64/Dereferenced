package com.mygdx.map;

import com.mygdx.messages.MSG;

/**
 * links tiles properties with MSG
 */
public enum TileReplacementEnum {
    WALL(MSG.BLOCK_WALLS),
    SWAP_FIGHT_STATE(MSG.SWAP_FIGHT_STATE);

    private final MSG msg;

    TileReplacementEnum(MSG msg) {
        this.msg = msg;
    }

    public MSG getMsg() {
        return msg;
    }
}
