import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableShellComponent } from './table-shell.component';

describe('TableShellComponent', () => {
  let component: TableShellComponent;
  let fixture: ComponentFixture<TableShellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableShellComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TableShellComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
