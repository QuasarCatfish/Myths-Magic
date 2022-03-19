package com.quas.mythsmagic.util;

import com.quas.mythsmagic.database.DB;
import com.quas.mythsmagic.database.Player;

public class Rating {

	public static final double BASE = 10;
	public static final double TENFOLD = 400;
	
	public static void updateRating(Player winner, Player loser, boolean tie) {
		updateRating(winner, tie ? Status.Tie : Status.Win, loser, tie ? Status.Tie : Status.Loss);
	}
	
	private static void updateRating(Player a, Status sa, Player b, Status sb) {
		// Calculate Q values
		double qa = Q(a);
		double qb = Q(b);
		
		// Calculate E values
		double ea = qa / (qa + qb);
		double eb = qb / (qa + qb);
		
		// Look up K values
		double ka = K(a);
		double kb = K(b);
		
		// Calculate new ratings
		double dra = ka * (sa.s - ea);
		double ra = a.getRating() + Math.signum(dra) * Math.max(1, Math.abs(dra));
		double drb = kb * (sb.s - eb);
		double rb = b.getRating() + Math.signum(drb) * Math.max(1, Math.abs(drb));
		
		// Update scores
		PlayerStatus[] players = Util.arr(new PlayerStatus(a, sa, ra), new PlayerStatus(b, sb, rb));
		for (PlayerStatus player : players) {
			if (player.status == Status.Win) DB.update("update `players` set `rating` = ?, `gamesWon` = `gamesWon` + 1 where `playerId` = ?;", player.rating, player.player.getPlayerId());
			if (player.status == Status.Tie) DB.update("update `players` set `rating` = ?, `gamesTied` = `gamesTied` + 1 where `playerId` = ?;", player.rating, player.player.getPlayerId());
			if (player.status == Status.Loss) DB.update("update `players` set `rating` = ?, `gamesLost` = `gamesLost` + 1 where `playerId` = ?;", player.rating, player.player.getPlayerId());
		}
	}
	
	private static double Q(Player p) {
		return Math.pow(BASE, p.getRating() / TENFOLD);
	}
	
	private static double K(Player p) {
		if (p.getGamesPlayed() < 20 && p.getRating() < 2000) return 40;
		if (p.getRating() < 2500) return 20;
		return 10;
	}
	
	public static double chanceToWin(Player player, Player opponent) {
		return Q(player) / (Q(player) + Q(opponent));
	}
	
	private enum Status {
		Win(1), Tie(0.5), Loss(0);
		
		private double s;
		private Status(double s) {
			this.s = s;
		}
	}
	
	private static class PlayerStatus {
		Player player;
		Status status;
		double rating;
		
		public PlayerStatus(Player player, Status status, double rating) {
			this.player = player;
			this.status = status;
			this.rating = rating;
		}
	}
}
