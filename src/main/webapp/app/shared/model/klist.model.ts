import { IKanban } from '@/shared/model/kanban.model';

export interface IKlist {
  id?: number;
  title?: string;
  order?: number | null;
  kanbanlist?: IKanban;
}

export class Klist implements IKlist {
  constructor(public id?: number, public title?: string, public order?: number | null, public kanbanlist?: IKanban) {}
}
