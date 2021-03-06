package ecp.reputation.scoring;

import java.util.List;

import ecp.reputation.sentiment.SentimentScore;

public class ScoreCalculator {

	public void genericCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall > 0) {
				positiveScore += sentimentScore.overall;
			} else {
				negativeScore += sentimentScore.overall;
			}
		}

		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("overall score is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void annotatedCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.annotated > 0) {
				positiveScore += sentimentScore.annotated;
			} else {
				negativeScore += sentimentScore.annotated;
			}
		}

		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("overall score with annotated weight is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void lnWeightedCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall != 0) {
				double tempscore = (double) sentimentScore.overall;
				if (sentimentScore.favorites > 2) {
					tempscore = tempscore * Math.log(sentimentScore.favorites);
				}
				if (sentimentScore.followers > 2) {
					tempscore = tempscore * Math.log(sentimentScore.followers);
				}
				if (sentimentScore.retweets > 2) {
					tempscore = tempscore * Math.log(sentimentScore.retweets);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("overall score with ln weight is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void lnWeightedAnnotatedCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.annotated != 0) {
				double tempscore = (double) sentimentScore.annotated;
				if (sentimentScore.favorites > 2) {
					tempscore = tempscore * Math.log(sentimentScore.favorites);
				}
				if (sentimentScore.followers > 2) {
					tempscore = tempscore * Math.log(sentimentScore.followers);
				}
				if (sentimentScore.retweets > 2) {
					tempscore = tempscore * Math.log(sentimentScore.retweets);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("annotated score with ln weight is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void lnplusWeightedCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		double coefficient = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall != 0) {
				double tempscore = (double) sentimentScore.overall;

				coefficient = Math.log(sentimentScore.favorites) + Math.log(sentimentScore.followers)
						+ Math.log(sentimentScore.retweets);

				if (coefficient > 2) {
					tempscore = tempscore * Math.log(coefficient);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("overall score with ln plus weight is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void lnplusWeightedAnnotatedCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		double coefficient = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.annotated != 0) {
				double tempscore = (double) sentimentScore.annotated;

				coefficient = Math.log(sentimentScore.favorites) + Math.log(sentimentScore.followers)
						+ Math.log(sentimentScore.retweets);

				if (coefficient > 2) {
					tempscore = tempscore * Math.log(coefficient);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("annotated score with ln plus weight is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void annotatedRatioCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		double coefficient = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.annotated != 0) {
				double tempscore = (double) sentimentScore.overall;
				if (sentimentScore.followers > 0) {
					if (sentimentScore.favorites > 0) {
						coefficient = (double) sentimentScore.favorites / (double) sentimentScore.followers;
					}
					if (sentimentScore.retweets > 0) {
						coefficient = coefficient
								+ (double) sentimentScore.retweets / (double) sentimentScore.followers;
					}
				}
				if (coefficient > 0) {
					tempscore = tempscore * (1 + coefficient);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("annotated score with ratio is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void RatioCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		double coefficient = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall != 0) {
				double tempscore = (double) sentimentScore.overall;
				if (sentimentScore.followers > 0) {
					if (sentimentScore.favorites > 0) {
						coefficient = (double) sentimentScore.favorites / (double) sentimentScore.followers;
					}
					if (sentimentScore.retweets > 0) {
						coefficient = coefficient
								+ (double) sentimentScore.retweets / (double) sentimentScore.followers;
					}
				}
				if (coefficient > 0) {
					tempscore = tempscore * (1 + coefficient);
				}
				if (tempscore > 0) {
					positiveScore += tempscore;
				} else {
					negativeScore += tempscore;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("score with ratio is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}

	public void BetaCalc(List<SentimentScore> scores) {
		double totalPos=0.0;
		double totalNeg=0.0;
		double totalPos_alt=0.0;
		double totalNeg_alt=0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall != 0) {
			//	if(sentimentScore.overall>0){
				totalPos=totalPos+(double)sentimentScore.positive+ sentimentScore.retweets* ((2*Math.pow((double)sentimentScore.positive, 2))/(((double)sentimentScore.negative+2)*((double)sentimentScore.positive+(double)sentimentScore.negative+2)+(2*(double)sentimentScore.positive)));
				totalPos_alt=totalPos_alt+(double)sentimentScore.positive+sentimentScore.retweets*((2*Math.pow((double)sentimentScore.positive, 2))/((Math.abs((double)sentimentScore.negative)+2)*((double)sentimentScore.positive+Math.abs((double)sentimentScore.negative)+2)+(2*(double)sentimentScore.positive)));
				
				//}else{
					totalNeg=totalNeg+(double)sentimentScore.negative+sentimentScore.retweets*((2*(double)sentimentScore.positive * (double)sentimentScore.negative)/(((double)sentimentScore.negative+2)*((double)sentimentScore.positive+(double)sentimentScore.negative+2)+(2*(double)sentimentScore.positive)));
				
				totalNeg_alt=totalNeg_alt+Math.abs((double)sentimentScore.negative)+sentimentScore.retweets*((2*(double)sentimentScore.positive * Math.abs((double)sentimentScore.negative))/((Math.abs((double)sentimentScore.negative)+2)*((double)sentimentScore.positive+Math.abs((double)sentimentScore.negative)+2)+(2*(double)sentimentScore.positive)));
			}
			//}
		}
		
		double finalScore=(totalPos-totalNeg)/(totalPos+totalNeg+2);
		double finalScore_alt=(totalPos_alt-totalNeg_alt)/(totalPos_alt+totalNeg_alt+2);
		
		System.out.println("positive "+totalPos);
		System.out.println("positive alt "+totalPos_alt);
		System.out.println("negative "+totalNeg);
		System.out.println("negative alt"+totalNeg_alt);
		System.out.println("final "+finalScore+ " or "+ (totalPos+totalNeg)/(totalPos+totalNeg+2)+ " or "  +(totalPos+totalNeg)/(totalPos-totalNeg+2));
		System.out.println("final alt "+finalScore_alt+ " or "+ (totalPos_alt+totalNeg_alt)/(totalPos+totalNeg_alt+2)+ " or "+ (totalPos_alt+totalNeg_alt)/(totalPos-totalNeg_alt+2));
	}
	
	public void positiveNegativeRatioCalc(List<SentimentScore> scores) {
		double positiveScore = 0.0;
		double negativeScore = 0.0;
		for (SentimentScore sentimentScore : scores) {
			if (sentimentScore.overall > 0) {
				if (sentimentScore.favorites > 0 && sentimentScore.followers > 0) {
					positiveScore = positiveScore + (1+(double) sentimentScore.favorites
							/ (double) sentimentScore.followers )* (double) sentimentScore.overall;
				} else {
					positiveScore = positiveScore + sentimentScore.overall;
				}
			}
			if (sentimentScore.overall < 0) {
				if (sentimentScore.favorites > 0 && sentimentScore.followers > 0) {
					positiveScore = positiveScore + 1+((double) sentimentScore.retweets
							/ (double) sentimentScore.followers) * (double) sentimentScore.overall;
				} else {
					negativeScore = negativeScore + sentimentScore.overall;
				}
			}
		}
		double total = positiveScore + Math.abs(negativeScore);

		System.out.println("score with negative positive ratio is +" + positiveScore + "  " + negativeScore + " +"
				+ positiveScore * 100 / total + " " + negativeScore * 100 / total);
	}
}
