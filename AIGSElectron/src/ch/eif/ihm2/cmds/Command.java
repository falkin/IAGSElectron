package ch.eif.ihm2.cmds;

import ch.eif.ihm2.model.IPlayerPlaying;

/**
 * Model for a command.
 * 
 * @author Michael Heinzer
 * @version 1.0 - 02.01.2012
 *
 */

public abstract class Command {

   IPlayerPlaying player;

   public Command(IPlayerPlaying p) {
      this.player = p;
   }

   public abstract void execute();
}