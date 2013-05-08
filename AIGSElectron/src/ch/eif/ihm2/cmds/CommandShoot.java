package ch.eif.ihm2.cmds;

import ch.eif.ihm2.model.IPlayerPlaying;

/**
 * Will be executed if a player presses the shoot key.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public class CommandShoot extends Command {
   public CommandShoot(IPlayerPlaying p) {
      super(p);
   }

   @Override
   public void execute() {
      player.requestShot();
   }
}