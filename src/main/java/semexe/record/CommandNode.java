package semexe.record;

/**
 * A command node basically takes the record node specified by the local
 * command environment and returns another record node.
 * General idea: a command node will first call its children
 * and process the result nodes returned to produce a single record node.
 */
public interface CommandNode extends RecordNode {
    RecordNode exec(LocalCommandEnv localEnv);
}
