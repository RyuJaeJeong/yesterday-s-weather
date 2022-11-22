import { LineModel } from './axis';
import { RectModel } from './series';

export type PlotModels = {
  plot: Array<LineModel | RectModel>;
  line: LineModel[];
  band: RectModel[];
};
