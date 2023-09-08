package auxiliaries;

import reinforcementlearning.ValueFunction;
import searchmethods.*;

public enum PolicyType {

    RANDOM_POLICY, MAX_VALUE_POLICY;

    public static Policy getPolicy(PolicyType type, ValueFunction vf) {
        switch (type) {
            case RANDOM_POLICY:
                return new RandomPolicy();
            case MAX_VALUE_POLICY:
                return new MaxValuePolicy(vf);
            default:
                throw new IllegalArgumentException("No such policy type: " + type);
        }
    }
}
