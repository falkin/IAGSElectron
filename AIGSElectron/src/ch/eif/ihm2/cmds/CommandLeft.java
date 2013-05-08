package ch.eif.ihm2.cmds;

import ch.eif.ihm2.model.Direction;
import ch.eif.ihm2.model.IPlayerPlaying;

/**
 * Will be executed if a player presses the left key.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class CommandLeft extends Command {
   public CommandLeft(IPlayerPlaying p) {
      super(p);
   }

   @Override
   public void execute() {
      if (player.getDirection() != Direction.RIGHT)
         player.setDirection(Direction.LEFT);
   }
}