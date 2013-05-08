package ch.eif.ihm2.cmds;

import ch.eif.ihm2.model.Direction;
import ch.eif.ihm2.model.IPlayerPlaying;

/**
 * Will be executed if a player presses the down key.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class CommandDown extends Command {
   public CommandDown(IPlayerPlaying p) {
      super(p);
   }

   @Override
   public void execute() {
      if (player.getDirection() != Direction.UP)
         player.setDirection(Direction.DOWN);
   }
}