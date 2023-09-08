package auxiliaries;

import neuralnetworks.*;

public enum BoardEncoderType {

    NAIVE_BOARD_ENCODER, EXTENDED_BOARD_ENCODER, FEATURE_BOARD_ENCODER;

    public static BoardEncoder getBoardEncoder(BoardEncoderType type) {
        switch (type) {
            case NAIVE_BOARD_ENCODER:
                return new NaiveBoardEncoder();
            case EXTENDED_BOARD_ENCODER:
                return new ExtendedBoardEncoder();
            case FEATURE_BOARD_ENCODER:
                return new FeatureBoardEncoder();
            default:
                throw new IllegalArgumentException("No such board encoder type: " + type);
        }
    }
}
