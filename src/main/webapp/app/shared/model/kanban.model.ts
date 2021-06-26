export interface IKanban {
  id?: number;
  name?: string;
  created_at?: Date | null;
  last_edit?: Date | null;
}

export class Kanban implements IKanban {
  constructor(public id?: number, public name?: string, public created_at?: Date | null, public last_edit?: Date | null) {}
}
