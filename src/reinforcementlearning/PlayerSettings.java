package reinforcementlearning;

import auxiliaries.*;

public class PlayerSettings {

    private PlayerType playerType;
    private BoardEncoderType boardEncoderType;
    private MoveSelectorType moveSelectorType;
    private SearchMethodType searchMethodType;
    private PolicyType policyType;
    private int searchBudget;

    public PlayerSettings(PlayerType playerType, BoardEncoderType boardEncoderType, MoveSelectorType moveSelectorType, SearchMethodType searchMethodType, PolicyType policyType, int searchBudget) {
        this.playerType = playerType;
        this.boardEncoderType = boardEncoderType;
        this.moveSelectorType = moveSelectorType;
        this.searchMethodType = searchMethodType;
        this.policyType = policyType;
        this.searchBudget = searchBudget;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public BoardEncoderType getBoardEncoderType() {
        return boardEncoderType;
    }

    public void setBoardEncoderType(BoardEncoderType boardEncoderType) {
        this.boardEncoderType = boardEncoderType;
    }

    public SearchMethodType getSearchMethodType() {
        return searchMethodType;
    }

    public void setSearchMethodType(SearchMethodType searchMethodType) {
        this.searchMethodType = searchMethodType;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public int getSearchBudget() {
        return searchBudget;
    }

    public void setSearchBudget(int searchBudget) {
        this.searchBudget = searchBudget;
    }

    public MoveSelectorType getMoveSelectorType() {
        return moveSelectorType;
    }

    public void setMoveSelectorType(MoveSelectorType moveSelectorType) {
        this.moveSelectorType = moveSelectorType;
    }

    @Override
    public String toString() {
        return "\tplayerType=" + playerType + PrintUtil.lineSeparators(1) + "\tboardEncoderType=" + boardEncoderType + PrintUtil.lineSeparators(1) + "\tmoveSelectorType=" + moveSelectorType + PrintUtil.lineSeparators(1) + "\tsearchMethodType=" + searchMethodType + PrintUtil.lineSeparators(1) + "\tpolicyType=" + policyType + PrintUtil.lineSeparators(1) + "\tsearchBudget=" + searchBudget + PrintUtil.lineSeparators(1);
    }
}
